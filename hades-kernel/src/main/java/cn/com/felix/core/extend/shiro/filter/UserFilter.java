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
 * File Name: UserFilter.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.shiro.filter;


import cn.com.felix.core.properties.ShiroProperties;
import cn.com.felix.core.utils.RequestUtils;
import cn.com.felix.core.utils.ResponseUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {
    private ShiroProperties shiroProperties;

    public ShiroProperties getShiroProperties() {
        return shiroProperties;
    }

    public void setShiroProperties(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, shiroProperties.isServiceOriented()))
            return super.onAccessDenied(request, response);

        ResponseUtils.responseInvalidLogin(response, shiroProperties.getInvalidLoginCode());
        return false;
    }
}
