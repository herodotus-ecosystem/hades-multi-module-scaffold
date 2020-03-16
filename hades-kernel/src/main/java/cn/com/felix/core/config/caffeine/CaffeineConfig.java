/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: CaffeineConfig
 * Author: gengwei.zheng
 * Date: 19-3-30 上午11:43
 * LastModified: 19-3-30 上午11:43
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
 * @author gengwei.zheng
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
