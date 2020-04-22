/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: APISmsController
 * Author: hades
 * Date: 19-3-18 下午3:54
 * LastModified: 19-3-18 下午3:54
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
