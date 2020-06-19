/*
 * Copyright (c) 2019-2020 the original author or authors.
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
 * Project Name: hades-platform
 * Module Name: hades-kernel
 * File Name: ModularSourceRealmAuthorizer.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.shiro.authz;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ModularSourceRealmAuthorizer extends ModularRealmAuthorizer {

    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        assertRealmsConfigured();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || principals.fromRealm(realm.getName()).isEmpty()) continue;
            if (((Authorizer) realm).hasRole(principals, roleIdentifier)) return true;
        }
        return false;
    }

    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        assertRealmsConfigured();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || principals.fromRealm(realm.getName()).isEmpty()) continue;
            if (((Authorizer) realm).isPermitted(principals, permission)) return true;
        }
        return false;
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        assertRealmsConfigured();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || principals.fromRealm(realm.getName()).isEmpty()) continue;
            if (((Authorizer) realm).isPermitted(principals, permission)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getRoles() throws InvocationTargetException, IllegalAccessException {
        assertRealmsConfigured();
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        List<String> roles = new ArrayList<>();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || principals.fromRealm(realm.getName()).isEmpty()) continue;

            Method getAuthorizationInfoMethod = ClassUtils.getMethod(realm.getClass(), "getAuthorizationInfo", PrincipalCollection.class);
            getAuthorizationInfoMethod.setAccessible(true);
            roles.addAll(((AuthorizationInfo) getAuthorizationInfoMethod.invoke(realm, principals)).getRoles());
        }
        return roles;
    }

    public List<String> getPermissions() throws InvocationTargetException, IllegalAccessException {
        assertRealmsConfigured();
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        List<String> permissions = new ArrayList<>();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || principals.fromRealm(realm.getName()).isEmpty()) continue;

            Method getAuthorizationInfoMethod = ClassUtils.getMethod(realm.getClass(), "getAuthorizationInfo", PrincipalCollection.class);
            getAuthorizationInfoMethod.setAccessible(true);
            permissions.addAll(((AuthorizationInfo) getAuthorizationInfoMethod.invoke(realm, principals)).getStringPermissions());
        }
        return permissions;
    }
}