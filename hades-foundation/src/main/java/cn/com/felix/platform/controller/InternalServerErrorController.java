/*
 * Copyright 2019-2019 the original author or authors.
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
 * Project Name: hades-multi-module
 * Module Name: hades-foundation
 * File Name: InternalServerErrorController.java
 * Author: gengwei.zheng
 * Date: 2019/11/3 下午3:34
 * LastModified: 2019/10/26 下午1:02
 */

package cn.com.felix.platform.controller;

import cn.com.felix.core.enums.ResultEnum;
import cn.com.felix.core.extend.exception.handler.GlobalExceptionHandler;
import cn.com.felix.core.extend.json.Result;
import cn.com.felix.core.extend.json.ResultUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

@Controller
public class InternalServerErrorController extends BasicErrorController {

    public InternalServerErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @RequestMapping(
            produces = {"text/html"}
    )
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = this.getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap(this.getErrorAttributes(request, this.isIncludeStackTrace(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        ModelAndView modelAndView = this.resolveErrorView(request, response, status, model);
        return modelAndView != null ? modelAndView : new ModelAndView(GlobalExceptionHandler.DEFAULT_ERROR_VIEW, model);
    }

    @RequestMapping("/401")
    public ModelAndView unauthorized(HttpServletRequest request) {
        Result result = ResultUtils.info(ResultEnum.UNAUTHORIZED);
        result.setPath(request.getRequestURL());
        return GlobalExceptionHandler.errorView(result);
    }

    @RequestMapping("/500")
    public ModelAndView internalServerError(HttpServletRequest request) {
        Result result = ResultUtils.error();
        result.setPath(request.getRequestURL());
        return GlobalExceptionHandler.errorView(result);
    }

    @RequestMapping("/418")
    public ModelAndView loginInvalid(HttpServletRequest request, HttpServletResponse response) {
        Result result = ResultUtils.info(ResultEnum.I_AM_A_TEAPOT);
        ModelAndView modelAndView = new ModelAndView("/login?invalid=2"); // 设置跳转路径
        modelAndView.addAllObjects(result); // 将异常对象传递过去
        modelAndView.setViewName("/login?invalid=2");
        return modelAndView;
    }
}
