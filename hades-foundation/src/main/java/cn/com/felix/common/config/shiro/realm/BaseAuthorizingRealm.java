package cn.com.felix.common.config.shiro.realm;

import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.domain.SysRole;
import cn.com.felix.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseAuthorizingRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(BaseAuthorizingRealm.class);


    protected AuthorizationInfo initAuthorizationInfo(SysUser sysUser) {

        List<String> sysRoles = new ArrayList<>();
        List<String> sysPermissions = new ArrayList<>();

        if (null != sysUser) {
            for (SysRole sysRole : sysUser.getRoles()) {
                sysRoles.add(sysRoles.size(), sysRole.getRoleName());
                log.debug("[Realm] |- User: [{}] Fetch Roles in {}", sysUser.getUserName(), sysRole.getRoleName());

                for (SysPermission sysPermission : sysRole.getPermissions()) {
                    if (StringUtils.isNotEmpty(sysPermission.getUrl())) {
                        sysPermissions.add(sysPermission.getUrl());
                        log.debug("[Realm] |- User: [{}] Fetch Permissions in {}", sysUser.getUserName(), sysPermission.getUrl());
                    }
                }
            }
        } else {
            throw new AuthenticationException();
        }

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(sysRoles);
        authorizationInfo.addStringPermissions(sysPermissions);

        return authorizationInfo;
    }
}
