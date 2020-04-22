/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: JwtHashedCredentialsMatcher
 * Author: hades
 * Date: 18-12-28 下午5:07
 * LastModified: 18-12-28 下午5:07
 */

package cn.com.felix.core.extend.shiro.authz;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2018/12/28
 */
public class JwtHashedCredentialsMatcher extends HashedCredentialsMatcher {

    /**
     * 注意坑点 : 密码校验 , 这里因为是JWT形式,就无需密码校验和加密,直接让其返回为true(如果不设置的话,该值默认为false,即始终验证不通过)
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return true;
    }
}
