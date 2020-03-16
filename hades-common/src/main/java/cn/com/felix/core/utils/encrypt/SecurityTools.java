/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: SecurityTools
 * Author: gengwei.zheng
 * Date: 19-3-19 下午12:45
 * LastModified: 19-3-19 下午12:45
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
 * @author gengwei.zheng
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
