/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: SmsService
 * Author: gengwei.zheng
 * Date: 19-3-18 下午3:32
 * LastModified: 19-3-18 下午3:32
 */

package cn.com.felix.core.service;

import cn.com.felix.core.extend.json.Result;
import cn.com.felix.core.utils.sms.QCloudSmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
 * @date 2019/3/18
 */
@Component
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private final QCloudSmsUtil qCloudSmsUtil;

    @Value("${spring.sms.type}")
    private String type;

    @Autowired
    public SmsService(QCloudSmsUtil qCloudSmsUtil) {
        this.qCloudSmsUtil = qCloudSmsUtil;
    }


    public void sendSms(String phoneNumber, String templateKey, ArrayList<String> param) {
        sendSms(phoneNumber, qCloudSmsUtil.getTemplateId(templateKey), param);
    }

    public void sendSms(String phoneNumber, int templateId, ArrayList<String> param) {
        if (qCloudSmsUtil.smsProperties.getActive()) {
            Result result = qCloudSmsUtil.send(phoneNumber, templateId, param);
            logger.debug("[SMS] |- Async Single Send Result: [{}]", result);

        } else {
            logger.info("[SMS] |- SMS is set to Active == false, if you want to continue, please change the setting!");
        }
    }

    public void sendSms(ArrayList<String> phoneNumbers, String templateKey, ArrayList<String> param) {
        sendSms(phoneNumbers, qCloudSmsUtil.getTemplateId(templateKey), param);
    }

    public void sendSms(ArrayList<String> phoneNumbers, int templateId, ArrayList<String> param) {
        if (qCloudSmsUtil.smsProperties.getActive()) {
            Result result = qCloudSmsUtil.batchSend(phoneNumbers, templateId, param);
            logger.debug("[SMS] |- Async Batch Send Result: [{}]", result);
        } else {
            logger.info("[SMS] |- SMS is set to Active == false, if you want to continue, please change the setting!");
        }
    }
}
