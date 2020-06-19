/*
 * Copyright (c) 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Project Name: hades-platform
 * Module Name: hades-data
 * File Name: RedisCaffeineCache.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.extend.cache.caffeine;

import cn.com.felix.core.properties.RedisCaffeineCacheProperties;
import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/3/30
 */
public class RedisCaffeineCache extends AbstractValueAdaptingCache {

    private static Logger logger = LoggerFactory.getLogger(RedisCaffeineCache.class);

    private String name;

    private RedisTemplate<Object, Object> stringKeyRedisTemplate;

    private Cache<Object, Object> caffeineCache;

    private String cachePrefix;

    private long defaultExpiration = 0;

    private Map<String, Long> expires;

    private String topic = "cache:redis:caffeine:topic";

    private Map<String, ReentrantLock> keyLockMap = new ConcurrentHashMap<>();

    protected RedisCaffeineCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    public RedisCaffeineCache(String name, RedisTemplate<Object, Object> stringKeyRedisTemplate,
                              Cache<Object, Object> caffeineCache, RedisCaffeineCacheProperties redisCaffeineCacheProperties) {
        super(redisCaffeineCacheProperties.isCacheNullValues());
        this.name = name;
        this.stringKeyRedisTemplate = stringKeyRedisTemplate;
        this.caffeineCache = caffeineCache;
        this.cachePrefix = redisCaffeineCacheProperties.getCachePrefix();
        this.defaultExpiration = redisCaffeineCacheProperties.getRedis().getDefaultExpiration();
        this.expires = redisCaffeineCacheProperties.getRedis().getExpires();
        this.topic = redisCaffeineCacheProperties.getRedis().getTopic();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = lookup(key);
        if (value != null) {
            return (T) value;
        }

        ReentrantLock lock = keyLockMap.get(key.toString());
        if (lock == null) {
            logger.debug("[Caffeine] |- Create lock for key : {}", key);
            lock = new ReentrantLock();
            keyLockMap.putIfAbsent(key.toString(), lock);
        }
        try {
            lock.lock();
            value = lookup(key);
            if (value != null) {
                return (T) value;
            }
            value = valueLoader.call();
            Object storeValue = toStoreValue(value);
            put(key, storeValue);
            return (T) value;
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e.getCause());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(Object key, Object value) {
        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
            return;
        }
        cachePut(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object cacheKey = getKey(key);
        Object prevValue = null;
        // 考虑使用分布式锁，或者将redis的setIfAbsent改为原子性操作
        synchronized (key) {
            prevValue = stringKeyRedisTemplate.opsForValue().get(cacheKey);
            if (prevValue == null) {
                cachePut(key, value);
            }
        }
        return toValueWrapper(prevValue);
    }

    private void cachePut(Object key, Object value) {
        long expire = getExpire();
        if (expire > 0) {
            stringKeyRedisTemplate.opsForValue().set(getKey(key), toStoreValue(value), expire, TimeUnit.MILLISECONDS);
        } else {
            stringKeyRedisTemplate.opsForValue().set(getKey(key), toStoreValue(value));
        }

        push(new CacheMessage(this.name, key));

        caffeineCache.put(key, toStoreValue(value));
    }

    @Override
    public void evict(Object key) {
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        stringKeyRedisTemplate.delete(getKey(key));

        push(new CacheMessage(this.name, key));

        caffeineCache.invalidate(key);
    }

    @Override
    public void clear() {
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        Set<Object> keys = stringKeyRedisTemplate.keys(this.name.concat(":*"));
        for (Object key : keys) {
            stringKeyRedisTemplate.delete(key);
        }

        push(new CacheMessage(this.name, null));

        caffeineCache.invalidateAll();
    }

    @Override
    protected Object lookup(Object key) {
        Object cacheKey = getKey(key);
        Object value = caffeineCache.getIfPresent(key);
        if (value != null) {
            logger.debug("[Caffeine] |- Get cache from caffeine, the key is : {}", cacheKey);
            return value;
        }

        value = stringKeyRedisTemplate.opsForValue().get(cacheKey);

        if (value != null) {
            logger.debug("[Caffeine] |- Get cache from redis and put in caffeine, the key is : {}", cacheKey);
            caffeineCache.put(key, value);
        }
        return value;
    }

    private Object getKey(Object key) {
        return this.name.concat(":").concat(StringUtils.isEmpty(cachePrefix) ? key.toString() : cachePrefix.concat(":").concat(key.toString()));
    }

    private long getExpire() {
        long expire = defaultExpiration;
        Long cacheNameExpire = expires.get(this.name);
        return cacheNameExpire == null ? expire : cacheNameExpire.longValue();
    }

    /**
     * @param message
     * @description 缓存变更时通知其他节点清理本地缓存
     * @author fuwei.deng
     * @date 2018年1月31日 下午3:20:28
     * @version 1.0.0
     */
    private void push(CacheMessage message) {
        stringKeyRedisTemplate.convertAndSend(topic, message);
    }

    /**
     * @param key
     * @description 清理本地缓存
     * @author fuwei.deng
     * @date 2018年1月31日 下午3:15:39
     * @version 1.0.0
     */
    public void clearLocal(Object key) {
        logger.debug("[Caffeine] |- clear local cache, the key is : {}", key);
        if (key == null) {
            caffeineCache.invalidateAll();
        } else {
            caffeineCache.invalidate(key);
        }
    }
}
