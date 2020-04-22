/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: QCloudSmsProperties
 * Author: hades
 * Date: 19-3-18 下午1:58
 * LastModified: 19-3-18 下午1:58
 */

package cn.com.felix.core.properties;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/3/18
 */

@Component
@ConfigurationProperties(prefix = "spring.sms.qcloud")
public class QCloudSmsProperties {
    private Integer appid;
    private String appkey;
    /**
     * 短信模板ID，需要在短信应用中申请
     */
    private Map<String, Integer> templates;
    /**
     * 签名，如果填空，系统会使用默认签名.签名，使用的是`签名内容`，而不是`签名ID`
     */
    private String sign = "";
    /**
     * 国家码，如 86 为中国
     */
    private String nationCode = "86";
    /**
     * 扩展码，可填空
     */
    private String extend = "";
    /**
     * 服务端原样返回的参数，可填空
     */
    private String ext = "";
    private Boolean active = false;

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Map<String, Integer> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, Integer> templates) {
        this.templates = templates;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("appid", appid)
                .append("appkey", appkey)
                .append("templates", templates)
                .append("sign", sign)
                .append("nationCode", nationCode)
                .append("extend", extend)
                .append("ext", ext)
                .append("active", active)
                .toString();
    }
}
