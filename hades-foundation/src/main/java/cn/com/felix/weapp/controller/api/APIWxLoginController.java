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
 * File Name: APIWxLoginController.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.weapp.controller.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.com.felix.common.config.jwt.JwtWxConfig;
import cn.com.felix.core.config.weapp.WxMaConfig;
import cn.com.felix.core.enums.ResultEnum;
import cn.com.felix.core.extend.json.Result;
import cn.com.felix.core.extend.json.ResultUtils;
import cn.com.felix.weapp.domain.WxRequest;
import cn.com.felix.weapp.service.WxSecurityService;
import cn.com.felix.weapp.service.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2018/12/29
 */
@RestController
@RequestMapping(value = "/open/")
@Api("为外部移动应用、小程序等接入提供服务的API,Open下的接口不需要授权")
public class APIWxLoginController {

    private static final Logger logger = LoggerFactory.getLogger(APIWxLoginController.class);

    @Resource
    private JwtWxConfig jwtWxConfig;
    private final WxUserService wxUserService;
    private final WxSecurityService wxSecurityService;

    @Autowired
    public APIWxLoginController(WxUserService wxUserService, WxSecurityService wxSecurityService) {
        this.wxUserService = wxUserService;
        this.wxSecurityService = wxSecurityService;
    }

    @Value("${wx.test}")
    private boolean isTest;

    @RequestMapping("/status")
    @ResponseBody
    public Result status(){

        Result result = new Result();
        result.setData(isTest);
        return  result;
    }


    @ApiOperation(value = "JWT登录系统，获取TokenAPI", notes = "只有首先获取到Token，后续API才可以正常使用。Token存储在请求头：x_auth_token中")
    @PostMapping("/login")
    @ResponseBody
    public Result login(@Valid WxRequest wxRequest) {

        final WxMaService wxService = WxMaConfig.getMaService(wxRequest.getAppId());

        Result result = new Result();
        try {
            WxMaJscode2SessionResult wxSession = wxService.getUserService().getSessionInfo(wxRequest.getCode());
            WxMaUserInfo wxUserInfo = wxUserService.storeWxUserInfo(wxRequest, wxSession);

            String token = generateToken(wxSession);
            result = ResultUtils.storeToken(token);
            result.put("userInfo", wxUserInfo);
            result.put("userSession", wxSession);

            boolean isWxUserBinding = wxSecurityService.isWxUserBinding(wxUserInfo.getOpenId());
            result.put("isWxUserBinding", isWxUserBinding);

            result.setStatus(ResultEnum.OK.getStatus());

        } catch (WxErrorException e) {
            result.setStatus(ResultEnum.UNAUTHORIZED.getStatus());
            result.setMessage("系统登录失败，请重稍后再试或者与管理员联系！");
            logger.error("[Weapp] |- WX User Login Failure!");
        }

        return result;
    }

    @ApiOperation(value = "重新获取Token", notes = "对于已经授权并且绑定的用户，就不需要重新进行绑定操作，通过该API进行重新授权。该接口不需要在进行用户信息的获取。")
    @PostMapping("/reAuthorize")
    @ResponseBody
    public Result reAuthorize(@Valid WxRequest wxRequest) {

        final WxMaService wxService = WxMaConfig.getMaService(wxRequest.getAppId());

        Result result = new Result();
        try {
            WxMaJscode2SessionResult wxSession = wxService.getUserService().getSessionInfo(wxRequest.getCode());
            String token = generateToken(wxSession);
            result = ResultUtils.storeToken(token);
            result.put("userSession", wxSession);
            result.setStatus(ResultEnum.OK.getStatus());
        } catch (WxErrorException e) {
            result.setStatus(ResultEnum.UNAUTHORIZED.getStatus());
            result.setMessage("系统授权失败，请重稍后再试或者与管理员联系！");
            logger.error("[Weapp] |- WX User eauthorize Failure!");
        }

        return result;
    }

    /**
     * 微信小程序用户登陆，完整流程可参考下面官方地址，本例中是按此流程开发
     * https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html
     * 1 . 我们的微信小程序端传入code。
     * 2 . 调用微信code2session接口获取openid和session_key
     * 3 . 根据openid和session_key自定义登陆态(Token)
     * 4 . 返回自定义登陆态(Token)给小程序端。
     * 5 . 我们的小程序端调用其他需要认证的api，请在header的Authorization里面携带 token信息
     *
     * @param wxSession 微信code2session接口
     * @return 返回后端 自定义登陆态 token  基于JWT实现
     */
    private String generateToken(WxMaJscode2SessionResult wxSession) {

        String token = jwtWxConfig.createTokenByWXSession(wxSession);

        if (logger.isDebugEnabled()) {
            logger.debug("[Miniapp] |- Create Token: [{}]", token);
        }

        return token;
    }
}
