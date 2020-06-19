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
 * File Name: HttpMethodPermissionFilter.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.shiro.filter;

import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpMethodPermissionFilter extends PermissionsAuthorizationFilter {
    private static final Logger log = LoggerFactory.getLogger(HttpMethodPermissionFilter.class);
    private final Map<String, String> httpMethodActions = new HashMap();

    public HttpMethodPermissionFilter() {
        HttpMethodPermissionFilter.HttpMethodAction[] var1 = HttpMethodPermissionFilter.HttpMethodAction.values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            HttpMethodPermissionFilter.HttpMethodAction methodAction = var1[var3];
            this.httpMethodActions.put(methodAction.name().toLowerCase(), methodAction.getAction());
        }

    }

    protected Map<String, String> getHttpMethodActions() {
        return this.httpMethodActions;
    }

    protected String getHttpMethodAction(ServletRequest request) {
        String method = ((HttpServletRequest) request).getMethod();
        return this.getHttpMethodAction(method);
    }

    protected String getHttpMethodAction(String method) {
        String lc = method.toLowerCase();
        String resolved = this.getHttpMethodActions().get(lc);
        return resolved != null ? resolved : method;
    }

    protected String[] buildPermissions(String[] configuredPerms, String action) {
        if (configuredPerms != null && configuredPerms.length > 0 && StringUtils.hasText(action)) {
            String[] mappedPerms = new String[configuredPerms.length];

            for (int i = 0; i < configuredPerms.length; ++i) {
                mappedPerms[i] = configuredPerms[i] + ":" + action;
            }

            if (log.isTraceEnabled()) {
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < mappedPerms.length; ++i) {
                    if (i > 0) {
                        sb.append(", ");
                    }

                    sb.append(mappedPerms[i]);
                }

                log.trace("MAPPED '{}' action to permission(s) '{}'", action, sb);
            }

            return mappedPerms;
        } else {
            return configuredPerms;
        }
    }

    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        String[] perms = (String[]) (mappedValue);
        String action = this.getHttpMethodAction(request);
        String[] resolvedPerms = this.buildPermissions(perms, action);
        return super.isAccessAllowed(request, response, resolvedPerms);
    }

    private enum HttpMethodAction {
        DELETE("delete"),
        GET("read"),
        HEAD("read"),
        MKCOL("create"),
        OPTIONS("read"),
        POST("create"),
        PUT("update"),
        TRACE("read");

        private final String action;

        HttpMethodAction(String action) {
            this.action = action;
        }

        public String getAction() {
            return this.action;
        }
    }
}
