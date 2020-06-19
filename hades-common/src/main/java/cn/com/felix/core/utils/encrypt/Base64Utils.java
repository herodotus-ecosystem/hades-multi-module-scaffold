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
 * File Name: Base64Utils.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.utils.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

@Slf4j
public class Base64Utils {

    private static final String UTF_8 = "UTF-8";

    /**
     * 对给定的字符串进行base64解码操作
     */
    public static String decodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.decodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            log.error(inputData, e);
        }

        return null;
    }

    /**
     * 对给定的字符串进行base64加密操作
     */
    public static String encodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.encodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            log.error(inputData, e);
        }

        return null;
    }
}
