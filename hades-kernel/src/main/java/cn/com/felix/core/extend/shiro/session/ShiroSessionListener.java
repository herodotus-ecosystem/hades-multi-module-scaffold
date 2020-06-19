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
 * File Name: ShiroSessionListener.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.shiro.session;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

public class ShiroSessionListener implements SessionListener {

    private static Logger logger = LoggerFactory.getLogger(ShiroSessionListener.class);

    private final AtomicInteger sessionCount = new AtomicInteger(0);

    @Resource
    private CachingSessionDAO sessionDAO;

    @Override
    public void onStart(Session session) {

        sessionCount.incrementAndGet();

        if (logger.isDebugEnabled()) {
            logger.debug("[Shiro] |- Listen >> Online User Added!!");
        }
    }

    @Override
    public void onStop(Session session) {

        sessionDAO.delete(session);

        sessionCount.decrementAndGet();

        if (logger.isDebugEnabled()) {
            logger.debug("[Shiro] |- Listen >> Online User Reduced!!");
        }
    }

    @Override
    public void onExpiration(Session session) {
        onStop(session);
    }

    /**
     * 获取在线人数使用
     * @return
     */
    public AtomicInteger getSessionCount() {
        return sessionCount;
    }
}
