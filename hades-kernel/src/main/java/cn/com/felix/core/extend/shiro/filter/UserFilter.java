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
