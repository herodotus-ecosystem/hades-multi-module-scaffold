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
