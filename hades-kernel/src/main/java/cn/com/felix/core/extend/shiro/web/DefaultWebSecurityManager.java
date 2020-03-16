package cn.com.felix.core.extend.shiro.web;

import cn.com.felix.core.extend.shiro.cache.ShiroRealmCacheManager;

public class DefaultWebSecurityManager extends org.apache.shiro.web.mgt.DefaultWebSecurityManager {
    @Override
    protected void afterRealmsSet() {
        super.afterRealmsSet();
        ShiroRealmCacheManager.setRealms(getRealms());
    }
}
