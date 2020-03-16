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
