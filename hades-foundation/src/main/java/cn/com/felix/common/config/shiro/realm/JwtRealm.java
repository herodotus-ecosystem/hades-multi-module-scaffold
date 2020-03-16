package cn.com.felix.common.config.shiro.realm;

import cn.com.felix.core.extend.shiro.token.JwtToken;
import cn.com.felix.common.config.jwt.JwtWxConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class JwtRealm extends BaseAuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(JwtRealm.class);

    @Resource
    private JwtWxConfig jwtWxConfig;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        if (logger.isDebugEnabled()) {
            logger.debug("[JWT Realm] |- Begin Authorization [AuthenticationToken: {}]", token.getCredentials());
        }

        String jwtToken = (String) token.getCredentials();
        String wxOpenId = jwtWxConfig.getWxOpenIdByToken(jwtToken);
        String sessionKey = jwtWxConfig.getSessionKeyByToken(jwtToken);

        if (StringUtils.isEmpty(wxOpenId)) {
            if (logger.isDebugEnabled()) {
                logger.debug("[JWT Realm] |- OpenId JWT.decode result is Empty!");
            }
            throw new AuthenticationException("user account not exits , please check your token");
        }
        if (StringUtils.isEmpty(sessionKey)) {
            if (logger.isDebugEnabled()) {
                logger.debug("[JWT Realm] |- sessionKey JWT.decode result is Empty!");
            }
            throw new AuthenticationException("sessionKey is invalid , please check your token");
        }
        if (!jwtWxConfig.verifyToken(jwtToken)) {
            if (logger.isDebugEnabled()) {
                logger.debug("[JWT Realm] |- JwtToken is invalid!");
            }
            throw new AuthenticationException("token is invalid , please check your token");
        }

        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
