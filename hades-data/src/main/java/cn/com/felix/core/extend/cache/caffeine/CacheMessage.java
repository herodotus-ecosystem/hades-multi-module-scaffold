/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: CacheMessage
 * Author: gengwei.zheng
 * Date: 19-3-30 上午11:13
 * LastModified: 19-3-30 上午11:13
 */

package cn.com.felix.core.extend.cache.caffeine;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
 * @date 2019/3/30
 */
public class CacheMessage implements Serializable {

    private String cacheName;

    private Object key;

    public CacheMessage(String cacheName, Object key) {
        super();
        this.cacheName = cacheName;
        this.key = key;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("cacheName", cacheName)
                .append("key", key)
                .toString();
    }
}
