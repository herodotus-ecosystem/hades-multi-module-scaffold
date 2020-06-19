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
 * Module Name: hades-common
 * File Name: SecurityTools.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.utils.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/3/19
 */
@Slf4j
public class SecurityTools {

    public static String encrypt(String content) {

        log.trace("[Security] |- Before Encrypt value: [{}]", content);

        String base64Content = Base64Utils.encodeData(content);

        log.trace("[Security] |- After Base64 Encrypt value: [{}]", base64Content);

        AesEncryptUtils aesEncryptUtils = new AesEncryptUtils();
        String result = aesEncryptUtils.encrypt(base64Content);
        log.trace("[Security] |- After AES Encrypt value: [{}]", result);

        return result.toUpperCase();
    }

    public static String decrypt(String content) {

        log.trace("[Security] |- Before Decrypt value: [{}]", content);

        String result = "";
        if (StringUtils.isNotEmpty(content)) {
            result = content.toLowerCase();
        }

        AesEncryptUtils aesEncryptUtils = new AesEncryptUtils();
        String aesContent = aesEncryptUtils.decrypt(result);

        log.trace("[Security] |- After AES Decrypt value: [{}]", aesContent);

        result = Base64Utils.decodeData(aesContent);

        log.trace("[Security] |- After Base64 Decrypt value: [{}]", result);


        return result;
    }

    public static void main(String[] args) throws Exception {

        String content = "元";
        String encrypt = SecurityTools.encrypt(content);
        System.out.println("加密：" + encrypt);

        List<String> list = new ArrayList<>();
//
        for (String abc : list
        ) {
            System.out.println("解密：" + SecurityTools.decrypt(abc));
        }
        String decrypt = SecurityTools.decrypt(encrypt);
        System.out.println("解密：" + decrypt);
    }
}
