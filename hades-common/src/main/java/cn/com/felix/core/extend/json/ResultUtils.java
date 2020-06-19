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
 * File Name: ResultUtils.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.json;

import cn.com.felix.core.enums.ResultEnum;
import cn.com.felix.core.extend.exception.SystemException;
import cn.com.felix.core.utils.HttpConstants;

public class ResultUtils {

    public static Result success() {
        return info(ResultEnum.OK);
    }

    public static Result error() {
        return info(ResultEnum.INTERNAL_SERVER_ERROR);
    }

    public static Result error(Exception e) {
        Result result = new Result();
        result.setStatus(ResultEnum.INTERNAL_SERVER_ERROR.getStatus());
        result.setMessage(e.getMessage());
        result.setError(e.getStackTrace());
        return result;
    }

    public static Result unauthorized() {
        return info(ResultEnum.UNAUTHORIZED);
    }

    public static Result unauthorized(String message) {
        return info(ResultEnum.UNAUTHORIZED.getStatus(), message);
    }

    public static Result info(int code, String message) {
        Result result = new Result();
        result.setStatus(code);
        result.setMessage(message);
        return result;
    }

    public static Result storeToken(String token) {
        Result result = info(ResultEnum.OK);
        result.put(HttpConstants.HEADER_X_AUTH_TOKEN, token);
        return result;
    }


    public static Result info(int code, String message, Object data) {
        Result result = new Result();
        result.setStatus(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result info(ResultEnum resultEnum) {
        return info(resultEnum.getStatus(), resultEnum.getMessage());
    }

    public static Result info(ResultEnum resultEnum, Object data) {
        return info(resultEnum.getStatus(), resultEnum.getMessage(), data);
    }

    public static Result error(SystemException systemException) {
        Result result = new Result();
        result.setStatus(systemException.getCode());
        result.setMessage(systemException.getMessage());
        result.setError(systemException.getStackTrace());
        return result;
    }
}
