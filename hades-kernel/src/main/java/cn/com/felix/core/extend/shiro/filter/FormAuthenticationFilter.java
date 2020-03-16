package cn.com.felix.core.extend.shiro.filter;

import cn.com.felix.core.properties.ShiroProperties;
import cn.com.felix.core.utils.RequestUtils;
import cn.com.felix.core.utils.ResponseUtils;
import com.google.common.collect.Maps;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(FormAuthenticationFilter.class);
    private ShiroProperties shiroProperties;

    public ShiroProperties getShiroProperties() {
        return shiroProperties;
    }

    public void setShiroProperties(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                if (log.isDebugEnabled()) {
                    log.debug("[Shiro Filter] |- Login submission detected.  Attempting to execute login.");
                }

                return this.executeLogin(request, response);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("[Shiro Filter] |- Login page view.");
                }

                return true;
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("[Shiro Filter] |- Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + this.getLoginUrl() + "]");
            }
            if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, shiroProperties.isServiceOriented())) {
                this.saveRequestAndRedirectToLogin(request, response);
            } else {
                ResponseUtils.responseInvalidLogin(response, shiroProperties.getInvalidLoginCode());
//                WebUtils.issueRedirect(request, response, "/login?invalid=2");
            }
            return false;
        }
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, shiroProperties.isServiceOriented())) {
            this.issueSuccessRedirect(request, response);
        } else {
            ResponseUtils.responseJson(response, HttpServletResponse.SC_OK, Maps.immutableEntry(shiroProperties.getLogInOutResponseKey(), true));
        }
        return false;
    }

    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("[Shiro Filter] |- Authentication exception", e);
        }

        if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, shiroProperties.isServiceOriented())) {
            this.setFailureAttribute(request, e);
            return true;
        }

        try {
            ResponseUtils.responseJson(response, HttpServletResponse.SC_OK, Maps.immutableEntry(shiroProperties.getLogInOutResponseKey(), false));
        } catch (IOException e1) {
            log.error("[Shiro Filter] |- Response login fail fails", e1);
        }
        return false;

    }
}
