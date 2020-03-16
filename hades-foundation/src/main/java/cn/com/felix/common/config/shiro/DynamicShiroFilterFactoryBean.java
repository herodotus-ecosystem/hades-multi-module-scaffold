/*
 * Copyright 2019-2019 the original author or authors.
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
 * Project Name: hades-multi-module
 * Module Name: hades-foundation
 * File Name: DynamicShiroFilterFactoryBean.java
 * Author: gengwei.zheng
 * Date: 2019/11/11 下午10:44
 * LastModified: 2019/11/11 下午10:43
 */

package cn.com.felix.common.config.shiro;

import cn.com.felix.common.config.shiro.realm.ShiroRealm;
import cn.com.felix.core.utils.Constants;
import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.domain.SysUser;
import cn.com.felix.system.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : gengwei.zheng
 * @description : TODO
 * @date : 2019/11/11 16:04
 */
@Slf4j
@Component
public class DynamicShiroFilterFactoryBean {

    private static final String URL_PERMISSION = "authc,perms[{0}]";

    @Autowired
    private SysPermissionService sysPermissionService;


    /**
     * anon（匿名）  org.apache.shiro.web.filter.authc.AnonymousFilter
     * authc（身份验证）       org.apache.shiro.web.filter.authc.FormAuthenticationFilter
     * authcBasic（http基本验证）    org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter
     * logout（退出）        org.apache.shiro.web.filter.authc.LogoutFilter
     * noSessionCreation（不创建session） org.apache.shiro.web.filter.session.NoSessionCreationFilter
     * perms(许可验证)  org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter
     * port（端口验证）   org.apache.shiro.web.filter.authz.PortFilter
     * rest  (rest方面)  org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter
     * roles（权限验证）  org.apache.shiro.web.filter.authz.RolesAuthorizationFilter
     * ssl （ssl方面）   org.apache.shiro.web.filter.authz.SslFilter
     * member （用户方面）  org.apache.shiro.web.filter.authc.UserFilter
     * user  表示用户不一定已通过认证,只要曾被Shiro记住过登录状态的用户就可以正常发起请求,比如rememberMe
     *
     * @return
     */
    public Map<String, String> loadFilterChainDefinitions() {
        //拦截器配置
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //过滤链定义，从上向下顺序执行，一般将 /** 放在最为下边。这是一个坑呢，一不小心代码就不好使了;
        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/", "user");
        filterChainDefinitionMap.put("/components/**", "anon");
        filterChainDefinitionMap.put("/features/**", "anon");
        filterChainDefinitionMap.put("/plugins/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        //增加该配置主要是因为访问首页的时候，该请求地址是500错误会导致找不到session
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/D5BEDD7FB93B3C591C868BDA631323A7", "anon");
        filterChainDefinitionMap.put("/flowable-task/**", "anon");
        filterChainDefinitionMap.put("/qr/**", "anon");
        filterChainDefinitionMap.put("/kaptcha", "anon");
        filterChainDefinitionMap.put("/api/**", "authc,roles[system]");
        filterChainDefinitionMap.put("/open/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "authc,roles[system]");
        // 1. 普通的action中 实现自己的logout方法，取到Subject，然后logout
        // 这种需要在ShiroFilterFactoryBean 中配置 filterChainDefinitions 对应的action的url为anon
        // 2. 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了.使用shiro提供的logout filter
        filterChainDefinitionMap.put("/logout", "logout");
        //配置记住我过滤器或认证通过可以访问的地址(当上次登录时，记住我以后，在下次访问/或/index时，可以直接访问，不需要登陆)

        filterChainDefinitionMap.putAll(obtainDynamicPermission());

        filterChainDefinitionMap.put("/**", "kickout,authc");

        return filterChainDefinitionMap;
    }

    private String checkUrlFormat(String url) {
        if (!url.startsWith(Constants.FORWARD_SLASH)) {
            StringBuilder result = new StringBuilder();
            result.append(Constants.FORWARD_SLASH);
            result.append(url);

            return result.toString();
        }

        return url;
    }

    /**
     * 从数据库中获取url资源信息，并拼凑成响应的权限。
     *
     * @return
     */
    private Map<String, String> obtainDynamicPermission() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        List<SysPermission> sysPermissions = sysPermissionService.findAll();
        if (CollectionUtils.isNotEmpty(sysPermissions)) {
            for (SysPermission sysPermission : sysPermissions) {
                String permissionUrl = sysPermission.getUrl();
                if (StringUtils.isNotEmpty(permissionUrl)) {

                    String url = checkUrlFormat(permissionUrl);

                    String settings = MessageFormat.format(URL_PERMISSION, url);
                    filterChainDefinitionMap.put(url, settings);

                    log.debug("[Shiro] |- Add Dynamic Permission [{} - {}]", url, settings);
                }
            }
        }

        return filterChainDefinitionMap;
    }

    /**
     * 更新权限,解决需要重启tomcat才能生效权限的问题
     */
    public void updateDynamicPermission() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            ServletContext servletContext = request.getSession().getServletContext();
            AbstractShiroFilter shiroFilter = (AbstractShiroFilter) WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).getBean("shiroFilter");
            synchronized (shiroFilter) {
                // 获取过滤管理器
                PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
                DefaultFilterChainManager defaultFilterChainManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
                // 清空初始权限配置
                defaultFilterChainManager.getFilterChains().clear();

                Map<String, String> chains = loadFilterChainDefinitions();
                chains.forEach((key, value) -> {
                    defaultFilterChainManager.createChain(key, value);
                    log.debug("[Shiro] |- Reload Permission [{}:{}]", key, value);
                });

                log.info("[Shiro] |- Updating Platform Permission Success!");
            }
        } catch (Exception e) {
            log.error("[Shiro] |- Updating Platform Permission Failed!", e);
        }

    }

    public void updateAllPermission() {
        updateDynamicPermission();
        updateUserPermission();
    }

    /**
     * 重新赋值权限(在比如:给一个角色临时添加一个权限,需要调用此方法刷新权限,否则还是没有刚赋值的权限)
     * @param sysUser
     */
    public void assginAuthorizingToAnotherUser(SysUser sysUser) {
        ShiroRealm shiroRealm = getShiroRealm();

        if (ObjectUtils.isNotEmpty(shiroRealm)) {

            Subject subject = SecurityUtils.getSubject();
            String realmName = subject.getPrincipals().getRealmNames().iterator().next();

            shiroRealm.getAuthorizationCache().remove(SecurityUtils.getSubject().getPrincipals());

            SimplePrincipalCollection principals = new SimplePrincipalCollection(sysUser, realmName);
            subject.runAs(principals);

            log.info("[Shiro] |- Assgin Authorizing To User [{}] Success!", sysUser.getUserName());
        } else {
            log.error("[Shiro] |- Assgin Authorizing To User Failed! Because Can not get Realm!");
        }
    }

    /**
     * 重新赋值权限(在比如:给一个角色临时添加一个权限,需要调用此方法刷新权限,否则还是没有刚赋值的权限)
     */
    public void updateUserPermission() {
        ShiroRealm shiroRealm = getShiroRealm();
        if (ObjectUtils.isNotEmpty(shiroRealm)) {
            shiroRealm.clearAllCachedAuthorizationInfo();
            log.debug("[Shiro] |- Clean Cached Authorization Info Success!");
        } else {
            log.error("[Shiro] |- Clean Cached Authorization Info Failed! Because Can not get Realm!");
        }
    }

    private ShiroRealm getShiroRealm() {
        SecurityManager securityManager = SecurityUtils.getSecurityManager();
        if (securityManager instanceof RealmSecurityManager) {
            RealmSecurityManager realmSecurityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();

            Realm realm = realmSecurityManager.getRealms().iterator().next();
            if (realm instanceof ShiroRealm) {
                return (ShiroRealm) realm;
            }
        }

        return null;
    }

}
