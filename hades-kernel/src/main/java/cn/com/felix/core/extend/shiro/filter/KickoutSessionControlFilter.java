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
 * File Name: KickoutSessionControlFilter.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.shiro.filter;

import cn.com.felix.core.extend.AbstractUser;
import cn.com.felix.core.extend.exception.SystemException;
import cn.com.felix.core.properties.ShiroProperties;
import cn.com.felix.core.utils.RequestUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public class KickoutSessionControlFilter extends AccessControlFilter {

    private static Logger logger = LoggerFactory.getLogger(KickoutSessionControlFilter.class);

    private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private Cache<String, Deque<Serializable>> kickoutCache;
    private SessionManager sessionManager;
    private ShiroProperties shiroProperties;

    public ShiroProperties getShiroProperties() {
        return shiroProperties;
    }

    public void setShiroProperties(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
        this.kickoutAfter = shiroProperties.isKickouAfter();
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.kickoutCache = cacheManager.getCache("realm:kickoutCache");
    }
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        Subject subject = getSubject(servletRequest, servletResponse);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        //如果有登录,判断是否访问的为静态资源，如果是游客允许访问的静态资源,直接返回true
        // 如果是静态文件，则返回true

        if (RequestUtils.isStaticResourcesRequest(servletRequest)){

            if (logger.isDebugEnabled()) {
                logger.debug("[Shiro Filter] |- Is Static Resource: [{}], Pass!!!!!", WebUtils.toHttp(servletRequest).getServletPath());
            }

            return true;
        }


        Session session = subject.getSession();
        Serializable sessionId = session.getId();

        String userId = null;
        if (subject.getPrincipal() != null && subject.getPrincipal() instanceof AbstractUser) {
            userId = ((AbstractUser)subject.getPrincipal()).getUid();
        }

        synchronized (this.kickoutCache) {
            if (kickoutCache == null) {
                throw new SystemException("Kickout Cache is not Created!");
            }

            //如果此用户没有session队列，也就是还没有登录过，缓存中没有
            //就new一个空队列，不然deque对象为空，会报空指针
            Deque<Serializable> deque = kickoutCache.get(userId);
            if (deque == null) {
                deque = new LinkedList<>();
            }

            //如果队列里没有此sessionId，且用户没有被踢出；放入队列
            if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
                deque.push(sessionId);
                kickoutCache.put(userId, deque);
            }

            logger.trace("[Shiro Filter] |- Login user is: [{}], Deque size is:[{}]", userId, deque.size());

            //如果队列里的sessionId数超出最大会话数，开始踢人
            while (deque.size() > shiroProperties.getKickoutMaxSession()) {
                Serializable kickoutSessionId = null;
                if (kickoutAfter) { //如果踢出后者
                    kickoutSessionId = deque.getFirst();
                    kickoutSessionId = deque.removeFirst();
                } else { //否则踢出前者
                    kickoutSessionId = deque.removeLast();
                }
                try {
                    Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                    if (kickoutSession != null) {
                        //设置会话的kickout属性表示踢出了
                        kickoutSession.setAttribute("kickout", true);
                    }
                } catch (Exception e) {//ignore exception
                    kickoutCache.put(userId, deque);

                    logger.trace("[Shiro Filter] |- Kickout Filter Remove Invalid Session ID: [{}]", kickoutSessionId);
                    logger.trace("[Shiro Filter] |- Current Kickout Deque: [{}]", deque.toString());
                }
            }

            Object kickout = session.getAttribute("kickout");

            //如果被踢出了，直接退出，重定向到踢出后的地址
            if (kickout != null) {
                //会话被踢出了
                try {
                    subject.logout();
                } catch (Exception e) {
                }
                WebUtils.issueRedirect(servletRequest, servletResponse, "/login?kickout=1");
                return false;
            }
        }
        return true;
    }
}
