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
 * File Name: WxBasicService.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.weapp.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.com.felix.core.config.weapp.WxMaConfig;
import cn.com.felix.core.extend.exception.SystemException;
import cn.com.felix.weapp.domain.WxRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WxBasicService {
    private static final Logger logger = LoggerFactory.getLogger(WxBasicService.class);

    public WxMaUserInfo decryptWxUserInfo(String sessionKey, WxRequest wxRequest) {

        final WxMaService wxService = WxMaConfig.getMaService(wxRequest.getAppId());

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, wxRequest.getRawData(), wxRequest.getSignature())) {
            logger.error("[Weapp] |- Decrypt WX User Info >> Signature can not Match!");
            throw new SystemException("Decrypt WX User Info >> Signature can not Match!");
        }

        // 解密用户信息
        WxMaUserInfo wxUserInfo = wxService.getUserService().getUserInfo(sessionKey, wxRequest.getEncryptedData(), wxRequest.getIv());

        return wxUserInfo;
    }

    public WxMaPhoneNumberInfo decryptWxPhoneNumberInfo(WxRequest wxRequest) {

        final WxMaService wxService = WxMaConfig.getMaService(wxRequest.getAppId());

        // 解密用户信息
        WxMaPhoneNumberInfo phoneNumberInfo = wxService.getUserService().getPhoneNoInfo(wxRequest.getSessionKey(), wxRequest.getEncryptedData(), wxRequest.getIv());

        return phoneNumberInfo;
    }
}
