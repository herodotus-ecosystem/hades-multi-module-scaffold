/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: RedisCaffeineCacheManager
 * Author: gengwei.zheng
 * Date: 19-3-30 上午11:17
 * LastModified: 19-3-30 上午11:17
 */

package cn.com.felix.core.extend.cache.caffeine;

import cn.com.felix.core.properties.RedisCaffeineCacheProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
 * @date 2019/3/30
 */
public class RedisCaffeineCacheManager implements CacheManager {

    private static Logger logger = LoggerFactory.getLogger(RedisCaffeineCacheManager.class);

    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private RedisCaffeineCacheProperties redisCaffeineCacheProperties;

    private RedisTemplate<Object, Object> stringKeyRedisTemplate;

    private boolean dynamic;

    private Set<String> cacheNames;

    public RedisCaffeineCacheManager(RedisCaffeineCacheProperties redisCaffeineCacheProperties,
                                     RedisTemplate<Object, Object> stringKeyRedisTemplate) {
        super();
        this.redisCaffeineCacheProperties = redisCaffeineCacheProperties;
        this.stringKeyRedisTemplate = stringKeyRedisTemplate;
        this.dynamic = redisCaffeineCacheProperties.isDynamic();
        this.cacheNames = redisCaffeineCacheProperties.getCacheNames();
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if (cache != null) {
            return cache;
        }
        if (!dynamic && !cacheNames.contains(name)) {
            return cache;
        }

        cache = new RedisCaffeineCache(name, stringKeyRedisTemplate, caffeineCache(), redisCaffeineCacheProperties);
        Cache oldCache = cacheMap.putIfAbsent(name, cache);
        logger.debug("[Caffeine] |- Create cache instance, the cache name is : {}", name);
        return oldCache == null ? cache : oldCache;
    }

    public com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache() {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if (redisCaffeineCacheProperties.getCaffeine().getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(redisCaffeineCacheProperties.getCaffeine().getExpireAfterAccess(), TimeUnit.MILLISECONDS);
        }
        if (redisCaffeineCacheProperties.getCaffeine().getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(redisCaffeineCacheProperties.getCaffeine().getExpireAfterWrite(), TimeUnit.MILLISECONDS);
        }
        if (redisCaffeineCacheProperties.getCaffeine().getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(redisCaffeineCacheProperties.getCaffeine().getInitialCapacity());
        }
        if (redisCaffeineCacheProperties.getCaffeine().getMaximumSize() > 0) {
            cacheBuilder.maximumSize(redisCaffeineCacheProperties.getCaffeine().getMaximumSize());
        }
        if (redisCaffeineCacheProperties.getCaffeine().getRefreshAfterWrite() > 0) {
            cacheBuilder.refreshAfterWrite(redisCaffeineCacheProperties.getCaffeine().getRefreshAfterWrite(), TimeUnit.MILLISECONDS);
        }
        return cacheBuilder.build();
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheNames;
    }

    public void clearLocal(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if (cache == null) {
            return;
        }

        RedisCaffeineCache redisCaffeineCache = (RedisCaffeineCache) cache;
        redisCaffeineCache.clearLocal(key);
    }
}
