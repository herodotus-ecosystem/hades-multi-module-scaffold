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
 * File Name: APISmsController.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.web.controller;

import cn.com.felix.core.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/3/18
 */
@RestController
@RequestMapping(value = "/open/sms")
public class APISmsController {

    private final SmsService smsService;

    @Autowired
    public APISmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @GetMapping("/singleSend")
    public void singleSend() {
        String phoneNumber = "136";
        int templateId = 295597;
        ArrayList<String> param = new ArrayList();
        param.add(param.size(), "信息");
        param.add(param.size(), "2019-03-25");
        param.add(param.size(), "726");

        smsService.sendSms(phoneNumber, templateId, param);
    }

    @GetMapping("/batchSend")
    public void batchSend() {
        ArrayList<String> phoneNumbers = new ArrayList();
        phoneNumbers.add(phoneNumbers.size(), "138");
        phoneNumbers.add(phoneNumbers.size(), "138");
        int templateId = 295597;
        ArrayList<String> param = new ArrayList();
        param.add(param.size(), "信息");
        param.add(param.size(), "2019-03-29");
        param.add(param.size(), "701");

        smsService.sendSms(phoneNumbers, templateId, param);
    }
}
