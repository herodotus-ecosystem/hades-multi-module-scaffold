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
 * Module Name: hades-foundation
 * File Name: PasswordHelper.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.common.config.shiro;

import cn.com.felix.core.properties.ShiroProperties;
import cn.com.felix.system.domain.SysUser;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordHelper {

    private static final Logger logger = LoggerFactory.getLogger(PasswordHelper.class);

    private final ShiroProperties shiroProperties;

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    @Autowired
    public PasswordHelper(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    /**
     * 将该用户输入的明文密码，根据HashedCredentialsMatcher逻辑进行加密。
     * @param sysUser 系统用户对象
     * @param plaintextPassword 用户输入的明文密码
     * @return
     */
    public SysUser encryptPassword(SysUser sysUser, String plaintextPassword) {

        String salt = randomNumberGenerator.nextBytes().toHex();
        sysUser.setSalt(salt);

        String newPassword = new SimpleHash(shiroProperties.getHashAlgorithmName(), plaintextPassword, ByteSource.Util.bytes(sysUser.getCredentialsSalt()), shiroProperties.getHashIterations()).toHex();
        if (logger.isDebugEnabled()) {
            logger.debug("[System] |-  Encrypt Password:[{}] to New Password:[{}] For User:[{}] By Salt:[{}]",sysUser.getPassword(), newPassword, sysUser.getUserName(), salt);
        }
        sysUser.setPassword(newPassword);

        return sysUser;
    }

    /**
     * 将该用户输入的明文密码，根据HashedCredentialsMatcher逻辑，与数据库中存储的密码进行比较。
     * @param sysUser
     * @param plaintextPassword
     * @return
     */
    public Boolean checkPassword(SysUser sysUser, String plaintextPassword) {
        String dbPassword = sysUser.getPassword();

        String encryptPassword = new SimpleHash(shiroProperties.getHashAlgorithmName(), plaintextPassword, ByteSource.Util.bytes(sysUser.getCredentialsSalt()), shiroProperties.getHashIterations()).toHex();

        return dbPassword.equals(encryptPassword);
    }

}
