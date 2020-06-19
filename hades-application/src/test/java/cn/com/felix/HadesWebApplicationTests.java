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
 * Module Name: hades-application
 * File Name: HadesWebApplicationTests.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix;

import cn.com.felix.core.utils.encrypt.AesEncryptUtils;
import org.apache.shiro.codec.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HadesWebApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    StringEncryptor stringEncryptor;

    @Test
    public void devPassord() {

        System.out.println("----------- dev -----------");

        String postgresqlUsername = stringEncryptor.encrypt("framework");
        String postgresqlPassword = stringEncryptor.encrypt("framework");

        System.out.println("Username - pg : " + postgresqlUsername);
        System.out.println("Password - pg : " + postgresqlPassword);

        Assert.assertTrue(postgresqlUsername.length() > 0);
        Assert.assertTrue(postgresqlPassword.length() > 0);
    }

    @Test
    public void prodPassord() {

        System.out.println("----------- prod -----------");

        String postgresqlUsername = stringEncryptor.encrypt("framework");
        String postgresqlPassword = stringEncryptor.encrypt("framework");
        String redisPassword = stringEncryptor.encrypt("framework");


        System.out.println("Username - pg : " + postgresqlUsername);
        System.out.println("Password - pg : " + postgresqlPassword);
        System.out.println("Password - redis : " + redisPassword);

        Assert.assertTrue(postgresqlUsername.length() > 0);
        Assert.assertTrue(postgresqlPassword.length() > 0);
        Assert.assertTrue(redisPassword.length() > 0);
    }

    @Test
    public void cipherkeyGenerate() throws NoSuchAlgorithmException {

        int length = 128;

        KeyGenerator keygen = KeyGenerator.getInstance("AES");;
//        keygen.init(length);
        SecretKey secretKey = keygen.generateKey();

        byte[] encodeKey = secretKey.getEncoded();
        String key = Base64.encodeToString(encodeKey);
        System.out.println(key);
    }

}

