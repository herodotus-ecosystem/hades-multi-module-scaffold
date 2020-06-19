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
 * File Name: AuthorizationWithStatusCodeFilter.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.shiro.filter;

import cn.com.felix.core.properties.ShiroProperties;
import cn.com.felix.core.utils.RequestUtils;
import cn.com.felix.core.utils.ResponseUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public abstract class AuthorizationWithStatusCodeFilter extends AuthorizationFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationWithStatusCodeFilter.class);

    private ShiroProperties shiroProperties;

    public ShiroProperties getShiroProperties() {
        return shiroProperties;
    }

    public void setShiroProperties(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, shiroProperties.isServiceOriented())) {

            if (logger.isDebugEnabled()) {
                logger.debug("[Shiro Filter] |- Access Denied.");
            }

            return super.onAccessDenied(request, response);
        }

        Subject subject = getSubject(request, response);

        if (subject.getPrincipal() == null) {
            ResponseUtils.responseInvalidLogin(response, shiroProperties.getInvalidLoginCode());
        } else {
            ResponseUtils.responseInvalidPermission(response, shiroProperties.getInvalidPermissionCode());
        }

        return false;
    }

}
