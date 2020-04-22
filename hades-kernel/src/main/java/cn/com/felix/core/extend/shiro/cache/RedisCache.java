/*
 * Copyright 2019-2019 the original author or authors.
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
 * Project Name: hades-multi-module
 * Module Name: hades-kernel
 * File Name: RedisCache.java
 * Author: hades
 * Date: 2019/11/10 下午12:07
 * LastModified: 2019/11/7 下午2:27
 */

package cn.com.felix.core.extend.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author hades
 */
public class RedisCache<K, V> implements Cache<K, V> {

    private static Logger logger = LoggerFactory.getLogger(RedisCache.class);

    public static final String REDIS_SHIRO_CACHE = "shiro:cache:";
    private String cacheKey;
    private RedisTemplate<K, V> redisTemplate;
    private long globExpire = 30;

    public RedisCache(String name, RedisTemplate redisTemplate) {
        this.cacheKey = REDIS_SHIRO_CACHE + name + ":";
        this.redisTemplate = redisTemplate;
    }

    @Override
    public V get(K key) throws CacheException {

        redisTemplate.boundValueOps(getCacheKey(key)).expire(globExpire, TimeUnit.MINUTES);

        logger.trace("[Redis] |- Redis Cache Get Cache With Key: [{}]", key);

        return redisTemplate.boundValueOps(getCacheKey(key)).get();
    }

    @Override
    public V put(K key, V value) throws CacheException {

        V old = get(key);
        redisTemplate.boundValueOps(getCacheKey(key)).set(value, globExpire, TimeUnit.MINUTES);

        logger.trace("[Redis] |- Redis Cache Put Cache With Key: [{}]", key);

        return old;
    }

    @Override
    public V remove(K key) throws CacheException {

        V old = get(key);
        redisTemplate.delete(getCacheKey(key));

        logger.trace("[Redis] |- Redis Cache Remove Cache With Key: [{}]", key);

        return old;
    }

    @Override
    public void clear() throws CacheException {

        logger.trace("[Redis] |- Redis Cache Clear Caches!!!!");

        redisTemplate.delete(keys());
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        return redisTemplate.keys(getCacheKey("*"));
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<>();
        for (K s : set) {
            list.add(get(s));
        }
        return list;
    }

    private K getCacheKey(Object k) {
        return (K) (this.cacheKey + k);
    }
}
