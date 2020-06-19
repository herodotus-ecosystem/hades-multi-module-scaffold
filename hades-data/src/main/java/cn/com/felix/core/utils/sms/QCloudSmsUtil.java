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
 * File Name: QCloudSmsUtil.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.utils.sms;

import cn.com.felix.core.extend.json.Result;
import cn.com.felix.core.properties.QCloudSmsProperties;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/3/18
 */
@Component
public class QCloudSmsUtil {

    private static final Logger logger = LoggerFactory.getLogger(QCloudSmsUtil.class);

    @Resource
    public QCloudSmsProperties smsProperties;

    public Result send(String phoneNumber, int templateId, ArrayList<String> params) {

        try {
            SmsSingleSender ssender = new SmsSingleSender(smsProperties.getAppid(), smsProperties.getAppkey());
            SmsSingleSenderResult smsSingleSenderResult = ssender.sendWithParam(smsProperties.getNationCode(), phoneNumber, templateId, params, smsProperties.getSign(), smsProperties.getExtend(), smsProperties.getExt());

            if (logger.isDebugEnabled()) {
                logger.debug("[QCloud SMS] |- Single Send Result: [{}]", smsSingleSenderResult.toString());
            }

            Result result = new Result();
            result.setStatus(smsSingleSenderResult.result);
            result.setMessage(smsSingleSenderResult.errMsg);
            result.put("sid", smsSingleSenderResult.sid);
            result.put("fee", smsSingleSenderResult.fee);
            result.put("ext", smsSingleSenderResult.ext);
            return result;
        } catch (HTTPException e) {
            logger.error("[QCloud SMS] |- Single Send HTTP Response Code Error!");
        } catch (IOException e) {
            logger.error("[QCloud SMS] |- Single Send Network IO Error!");
        } catch (JSONException e) {
            logger.error("[QCloud SMS] |- Single Send JSON Parse Error!");
        }

        return null;

    }

    public Result batchSend(ArrayList<String> phoneNumbers, int templateId, ArrayList<String> params) {

        try {
            SmsMultiSender msender = new SmsMultiSender(smsProperties.getAppid(), smsProperties.getAppkey());
            SmsMultiSenderResult smsMultiSenderResult = msender.sendWithParam(smsProperties.getNationCode(), phoneNumbers, templateId, params, smsProperties.getSign(), smsProperties.getExtend(), smsProperties.getExt());

            if (logger.isDebugEnabled()) {
                logger.debug("[QCloud SMS] |- Batch Send Result: [{}]", smsMultiSenderResult.toString());
            }

            Result result = new Result();
            result.setStatus(smsMultiSenderResult.result);
            result.setMessage(smsMultiSenderResult.errMsg);
            result.put("detail", smsMultiSenderResult.details);
            result.put("ext", smsMultiSenderResult.ext);

            return result;
        } catch (HTTPException e) {
            logger.error("[QCloud SMS] |- Batch Send HTTP Response Code Error!");
        } catch (IOException e) {
            logger.error("[QCloud SMS] |- Batch Send Network IO Error!");
        } catch (JSONException e) {
            logger.error("[QCloud SMS] |- Batch Send JSON Parse Error!");
        }

        return null;
    }

    public int getTemplateId(String templateKey) {
        return smsProperties.getTemplates().get(templateKey);
    }
}
