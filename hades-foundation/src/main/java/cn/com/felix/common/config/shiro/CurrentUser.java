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
 * Module Name: hades-foundation
 * File Name: CurrentUser.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.common.config.shiro;

import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author hades
 */
@Slf4j
public final class CurrentUser {

    private static final Logger logger = LoggerFactory.getLogger(CurrentUser.class);

    public CurrentUser() {
    }

    /**
     * 增加了JWT，如果是JWT调用，会返回JWTToken类型，导致Spring boot AuditorAware出错
     *
     * @return
     */
    public static SysUser getPrincipal() {
        Subject subject = getSubject();

        if (null != subject) {
            if (subject.getPrincipal() instanceof SysUser) {
                return (SysUser) subject.getPrincipal();
            }
        }

        return null;
    }

    public static String getUserName() {
        if (null != getPrincipal()) {
            return getPrincipal().getUserName();
        }
        return null;
    }

    public static String getUserID() {
        if (null != getPrincipal()) {
            return getPrincipal().getUid();
        }
        return null;
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Session getSession() {
        try {
            Session session = getSubject().getSession();
            if (session == null) {
                session = getSubject().getSession();
            }
            if (session != null) {
                return session;
            }
        } catch (InvalidSessionException e) {
            logger.warn("-| [Shiro] Invalid Session!");
        }
        return null;
    }

    public static void setAttribute(Object key, Object value) {

    }

    public static PrincipalCollection getPrincipals() {
        return getSubject().getPrincipals();
    }

    public static SecurityManager getSecurityManager() {
        return SecurityUtils.getSecurityManager();
    }

    public static List<SysPermission> getMenu() {

        SysUser sysUser = getPrincipal();

        List<SysPermission> sysPermissions = new ArrayList<>();

        if (null != sysUser) {
            sysPermissions = sysUser.getRoles().stream()
                    .flatMap(sysRole -> sysRole.getPermissions().stream())
                    .filter(sysPermission -> null == sysPermission.getParent())
                    .sorted(Comparator.comparing(SysPermission::getRanking))
                    .collect(toList());

        }

        return sysPermissions;
    }
}
