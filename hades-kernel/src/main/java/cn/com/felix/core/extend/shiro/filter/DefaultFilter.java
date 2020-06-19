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
 * File Name: DefaultFilter.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.shiro.filter;

import cn.com.felix.core.properties.ShiroProperties;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.session.NoSessionCreationFilter;

import javax.servlet.Filter;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public enum DefaultFilter {

    anon(AnonymousFilter.class),
    authc(FormAuthenticationFilter.class),
    authcBasic(BasicHttpAuthenticationFilter.class),
    logout(LogoutFilter.class),
    noSessionCreation(NoSessionCreationFilter.class),
    perms(PermissionsAuthorizationFilter.class),
    rest(HttpMethodPermissionFilter.class),
    roles(RolesAuthorizationFilter.class),
    anyRoles(AnyRolesAuthorizationFilter.class),
    user(UserFilter.class),
    kickout(KickoutSessionControlFilter.class),
    jwt(JwtAuthenticationFilter.class);

    private final Class<? extends Filter> filterClass;

    DefaultFilter(Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }

    public static Map<String, Filter> createInstanceMap(ShiroProperties shiroProperties, SessionManager sessionManager, CacheManager cacheManager) throws InvocationTargetException, IllegalAccessException {
        Map<String, Filter> filters = new LinkedHashMap<>(values().length);
        for (DefaultFilter defaultFilter : values()) {
            Filter filter = defaultFilter.newInstance();
            BeanUtils.setProperty(filter, "shiroProperties", shiroProperties);
            BeanUtils.setProperty(filter, "sessionManager", sessionManager);
            BeanUtils.setProperty(filter, "cacheManager", cacheManager);
            filters.put(defaultFilter.name(), filter);
        }
        return filters;
    }

    public Filter newInstance() {
        return (Filter) ClassUtils.newInstance(this.filterClass);
    }

}
