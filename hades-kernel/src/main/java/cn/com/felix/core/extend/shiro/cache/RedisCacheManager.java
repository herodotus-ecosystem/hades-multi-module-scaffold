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
 * File Name: RedisCacheManager.java
 * Author: hades
 * Date: 2019/11/10 下午12:07
 * LastModified: 2019/11/7 下午2:27
 */

package cn.com.felix.core.extend.shiro.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author hades
 */
public class RedisCacheManager extends AbstractCacheManager {

    private static Logger logger = LoggerFactory.getLogger(RedisCache.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    protected Cache createCache(String name) throws CacheException {
        logger.trace("[Redis] |- Redis Cache Manager Create Cache: [{}]!!!!", name);

        return new RedisCache(name, redisTemplate);
    }
}
