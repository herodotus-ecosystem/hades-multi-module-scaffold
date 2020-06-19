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
 * File Name: JwtAuthenticationFilter.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.shiro.filter;

import cn.com.felix.core.extend.shiro.token.JwtToken;
import cn.com.felix.core.properties.ShiroProperties;
import cn.com.felix.core.utils.HttpConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 参考：
 *
 * @link：https://github.com/chilexun/springboot-demo
 * @link：https://www.jianshu.com/p/0b1131be7ace
 * @link：https://github.com/Smith-Cruise/Spring-Boot-Shiro
 * @link：https://segmentfault.com/a/1190000017212730
 * JWT核心过滤器配置
 * 所有的请求都会先经过Filter，所以我们继承官方的BasicHttpAuthenticationFilter，并且重写鉴权的方法。
 * 执行流程 preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 */
public class JwtAuthenticationFilter extends BasicHttpAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private Cache<String, Object> jwtAuthenticationCache;
    private ShiroProperties shiroProperties;

    public void setShiroProperties(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.jwtAuthenticationCache = cacheManager.getCache("realm:wxJwtTokenCache");
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String auth = getAuthzHeader(request);
        return new JwtToken(auth);
    }

    /**
     * 判断用户是否想要进行 需要验证的操作
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String auth = getAuthzHeader(request);
        return StringUtils.isNotEmpty(auth);
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        JwtToken token = new JwtToken(getAuthzHeader(request));
        getSubject(request, response).login(token);
        return true;
    }

    /**
     * 此方法调用登陆，验证逻辑
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse);
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        this.fillCorsHeader(httpServletRequest, httpServletResponse);
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String header = httpRequest.getHeader("Authorization");
        return StringUtils.removeStart(header, HttpConstants.HEADER_BEARER);
    }

    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader(HttpConstants.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        httpServletResponse.setHeader(HttpConstants.HEADER_ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,OPTIONS,HEAD,PUT,DELETE");
        httpServletResponse.setHeader(HttpConstants.HEADER_ACCESS_CONTROL_MAX_AGE, "7200");
        httpServletResponse.setHeader(HttpConstants.HEADER_ACCESS_CONTROL_ALLOW_HEADERS, "Origin,X-Requested-With,Content-Type,Accept,Authorization");
        httpServletResponse.setHeader(HttpConstants.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
    }
}
