package cn.com.felix.common.config.shiro;
import cn.com.felix.core.config.redis.RedisConfig;

import cn.com.felix.common.config.shiro.realm.JwtRealm;
import cn.com.felix.common.config.shiro.realm.ShiroRealm;
import cn.com.felix.core.extend.shiro.cache.RedisCacheManager;
import cn.com.felix.core.extend.shiro.authz.ModularSourceRealmAuthorizer;
import cn.com.felix.core.extend.shiro.authz.RetryLimitHashedCredentialsMatcher;
import cn.com.felix.core.extend.shiro.filter.DefaultFilter;
import cn.com.felix.core.extend.shiro.session.RedisSessionDAO;
import cn.com.felix.core.extend.shiro.session.ShiroSessionFactory;
import cn.com.felix.core.extend.shiro.session.ShiroSessionListener;
import cn.com.felix.core.extend.shiro.web.CookieRememberMeManager;
import cn.com.felix.core.extend.shiro.web.DefalutWebSessionManager;
import cn.com.felix.core.extend.shiro.web.DefaultWebSecurityManager;
import cn.com.felix.core.properties.ShiroProperties;
import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.service.SysPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AbstractAuthenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hades
 */
@Configuration
@ConditionalOnBean(value = {RedisConfig.class})
@EnableConfigurationProperties({
        ShiroProperties.class
})
public class ShiroConfig {

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Autowired
    private DynamicShiroFilterFactoryBean dynamicShiroFilterFactoryBean;
    private final ShiroProperties shiroProperties;

    @Autowired
    public ShiroConfig(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    @Bean(name = "shiroFilter")
    @ConfigurationProperties(prefix = "shiro")
    public ShiroFilterFactoryBean shiroFilter(
            SecurityManager securityManager,
            WebSessionManager sessionManager,
            RedisCacheManager redisCacheManager) throws InvocationTargetException, IllegalAccessException {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(DefaultFilter.createInstanceMap(shiroProperties, sessionManager, redisCacheManager));

        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(shiroProperties.getSuccessUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());

        shiroFilterFactoryBean.setFilterChainDefinitionMap(dynamicShiroFilterFactoryBean.loadFilterChainDefinitions());

        return shiroFilterFactoryBean;
    }



    @Bean(name = "redisCacheManager")
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager();
    }

    /**
     * 身份认证realm;
     * (这个需要自己写，账号密码校验；权限等)
     */
    @Bean(name = "shiroRealm")
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        //告诉realm,使用credentialsMatcher加密算法类来验证密文
        shiroRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
        //启用缓存,默认false
        shiroRealm.setCachingEnabled(true);
        //  启用身份验证缓存，即缓存AuthenticationInfo信息，默认false；
        shiroRealm.setAuthenticationCachingEnabled(true);
        //  启用授权缓存，即缓存AuthorizationInfo信息，默认false；
        shiroRealm.setAuthorizationCachingEnabled(true);
        //  缓存AuthenticationInfo信息的缓存名称,即配置在ehcache.xml中的cache name
        shiroRealm.setAuthenticationCacheName("realm:authenticationCache");
        //  缓存AuthorizationInfo信息的缓存名称；
        shiroRealm.setAuthorizationCacheName("realm:authorizationCache");

        return shiroRealm;
    }

    @Bean(name = "jwtRealm")
    public JwtRealm jwtRealm() {
        JwtRealm jwtRealm = new JwtRealm();
        //告诉realm,使用credentialsMatcher加密算法类来验证密文
        jwtRealm.setCredentialsMatcher(credentialsMatcher());

        //启用缓存,默认false
        jwtRealm.setCachingEnabled(true);
        //  启用身份验证缓存，即缓存AuthenticationInfo信息，默认false；
        jwtRealm.setAuthenticationCachingEnabled(true);
        //  启用授权缓存，即缓存AuthorizationInfo信息，默认false；
        jwtRealm.setAuthorizationCachingEnabled(true);
        //  缓存AuthenticationInfo信息的缓存名称,即配置在ehcache.xml中的cache name
        jwtRealm.setAuthenticationCacheName("realm:authenticationCache");
        //  缓存AuthorizationInfo信息的缓存名称；
        jwtRealm.setAuthorizationCacheName("realm:authorizationCache");

        return jwtRealm;
    }

    /**
     * 注意坑点 : 密码校验 , 这里因为是JWT形式,就无需密码校验和加密,直接让其返回为true(如果不设置的话,该值默认为false,即始终验证不通过)
     */
    private CredentialsMatcher credentialsMatcher() {
        return (token, info) -> true;
    }



    /**
     * 凭证匹配器
     * 由于的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了，所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     */
    @Bean(name = "credentialsMatcher")
    public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher() {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(redisCacheManager(),shiroProperties.getErrorTimes(),shiroProperties.getRetryLimit());
        return credentialsMatcher;
    }

    @Bean
    public SecurityManager securityManager(
            WebSessionManager sessionManager,
            AbstractAuthenticator authenticator,
            RedisCacheManager redisCacheManager,
            RememberMeManager rememberMeManager,
            ModularRealmAuthorizer modularRealmAuthorizer
    ) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(authenticator);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(redisCacheManager);
        securityManager.setRememberMeManager(rememberMeManager);
        securityManager.setAuthorizer(modularRealmAuthorizer);

        List<Realm> realms = new ArrayList<>();
        realms.add(shiroRealm());
        realms.add(jwtRealm());

        securityManager.setRealms(realms);
        return securityManager;
    }

    @Bean
    public ModularRealmAuthorizer modularRealmAuthorizer() {
        return new ModularSourceRealmAuthorizer();
    }

    @Bean
    @ConfigurationProperties(prefix = "shiro.remember-me")
    public RememberMeManager rememberMeManager() {
        return new CookieRememberMeManager();
    }


    @Bean
    @ConfigurationProperties(prefix = "shiro.session")
    public WebSessionManager sessionManager(
            SessionDAO sessionDAO,
            SessionFactory sessionFactory,
            SessionListener sessionListener) {
        DefalutWebSessionManager defaultWebSessionManager = new DefalutWebSessionManager();
        defaultWebSessionManager.setSessionDAO(sessionDAO);
        defaultWebSessionManager.setSessionFactory(sessionFactory);

        List<SessionListener> listeners = new ArrayList<>();
        listeners.add(sessionListener);
        defaultWebSessionManager.setSessionListeners(listeners);

        return defaultWebSessionManager;
    }

    @Bean(name = "sessionDAO")
    public CachingSessionDAO sessionDAO() {
        CachingSessionDAO cachingSessionDAO = new RedisSessionDAO();

        if (cachingSessionDAO != null) {
            cachingSessionDAO.setActiveSessionsCacheName("realm:activeSessionCache");
        }

        return cachingSessionDAO;
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new ShiroSessionFactory();
    }

    @Bean
    public SessionListener sessionListener() {
        return new ShiroSessionListener();
    }

    @Bean
    public AbstractAuthenticator authenticator() {
        return new ModularRealmAuthenticator();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(ShiroFilterFactoryBean shiroFilterFactoryBean) throws Exception {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setFilter((Filter) shiroFilterFactoryBean.getObject());
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
