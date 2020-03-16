/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: CacheEventLisener
 * Author: gengwei.zheng
 * Date: 19-3-30 下午3:00
 * LastModified: 19-3-30 下午3:00
 */

package cn.com.felix.core.extend.cache.ehcache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
 * @date 2019/3/30
 */
public class EhcacheEventLisener implements CacheEventListener<Object, Object> {

    private static Logger logger = LoggerFactory.getLogger(EhcacheEventLisener.class);

    @Override
    public void onEvent(CacheEvent<?, ?> event) {
        logger.debug("Event: " + event.getType() + " Key: " + event.getKey() + " old value: " + event.getOldValue() + " new value: " + event.getNewValue());
    }
}
