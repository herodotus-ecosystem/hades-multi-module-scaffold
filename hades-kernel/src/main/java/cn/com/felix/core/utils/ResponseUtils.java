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
 * File Name: ResponseUtils.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class ResponseUtils {


    public static void response(ServletResponse response, MediaType mediaType, int statusCode, String message) throws IOException {


        log.debug("[Response Utils] |- Write Response with Status : [{}], and Message : [{}]", statusCode, message);

        HttpServletResponse resp = WebUtils.toHttp(response);
        resp.setContentType(mediaType.toString());
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setStatus(statusCode);
        PrintWriter writer = resp.getWriter();
        writer.write(message);
        writer.flush();
    }

    public static void responseJson(ServletResponse response, int statusCode, Object message) throws IOException {
        response(response, MediaType.APPLICATION_JSON_UTF8, statusCode, JSON.toJSONString(message));
    }

    public static void responseInvalidLogin(ServletResponse response, int statusCode) throws IOException {
        response(response, MediaType.ALL, statusCode, "login state is invalid");
    }

    public static void responseInvalidPermission(ServletResponse response, int statusCode) throws IOException {
        response(response, MediaType.ALL, statusCode, "you have no enough permission to access");
    }

}
