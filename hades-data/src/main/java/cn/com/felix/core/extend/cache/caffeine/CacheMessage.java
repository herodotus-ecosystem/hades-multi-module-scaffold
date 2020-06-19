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
 * File Name: CacheMessage.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.extend.cache.caffeine;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <p>Description: </p>
 *
 * @author hades
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
