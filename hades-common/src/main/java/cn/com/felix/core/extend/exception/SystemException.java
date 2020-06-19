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
 * File Name: SystemException.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.exception;

import cn.com.felix.core.enums.ResultEnum;

public class SystemException extends RuntimeException {
    private String message;
    private int code = ResultEnum.UNKONWN_ERROR.getStatus();

    public SystemException(String message) {
        super(message);
        this.message = message;
    }

    public SystemException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public SystemException(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public SystemException(String message, int code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }

    public SystemException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.message = resultEnum.getMessage();
        this.code = resultEnum.getStatus();
    }

    public SystemException(ResultEnum resultEnum, Throwable e) {
        super(resultEnum.getMessage(), e);
        this.message = resultEnum.getMessage();
        this.code = resultEnum.getStatus();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
