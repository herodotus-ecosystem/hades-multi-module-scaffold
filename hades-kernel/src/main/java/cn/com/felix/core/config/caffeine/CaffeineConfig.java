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
 * Module Name: hades-kernel
 * File Name: CaffeineConfig.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.config.caffeine;

import cn.com.felix.core.extend.cache.caffeine.CacheMessageListener;
import cn.com.felix.core.extend.cache.caffeine.RedisCaffeineCacheManager;
import cn.com.felix.core.properties.RedisCaffeineCacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/3/30
 */
public class CaffeineConfig {

    private RedisCaffeineCacheProperties redisCaffeineCacheProperties;


    public RedisCaffeineCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisCaffeineCacheManager(redisCaffeineCacheProperties, redisTemplate);
    }

    public RedisMessageListenerContainer redisMessageListenerContainer(RedisTemplate<Object, Object> redisTemplate, RedisCaffeineCacheManager redisCaffeineCacheManager) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisTemplate.getConnectionFactory());
        CacheMessageListener cacheMessageListener = new CacheMessageListener(redisTemplate, redisCaffeineCacheManager);
        redisMessageListenerContainer.addMessageListener(cacheMessageListener, new ChannelTopic(redisCaffeineCacheProperties.getRedis().getTopic()));
        return redisMessageListenerContainer;
    }
}
