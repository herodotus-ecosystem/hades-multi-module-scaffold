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
 * Module Name: hades-data
 * File Name: SmsService.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
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
 * @author hades
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
