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
 * File Name: ShiroProperties.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private String successUrl;
    private String loginUrl;
    private String unauthorizedUrl;
    private int errorTimes;
    private String hashAlgorithmName;
    private int hashIterations;
    private boolean storedCredentialsHexEncoded = false;
    private int retryLimit;
    private int invalidLoginCode = 418;
    private int invalidPermissionCode = 401;
    private boolean serviceOriented = false;
    private String logInOutResponseKey = "success";
    /**
     * 全部需要Cache的信息是否是缓存至Redis中。
     * false为缓存至Ehcache， true为缓存至Redis
     */
    private boolean switchCacheToRedis = true;
    /**
     * 现有代码支持Redis和Ehcache共存。共存的方式为主要缓存信息还存在Ehcache中，Session ID 信息使用RedisSessionDAO，在Redis中存储。以起到共享作用。
     */
    private boolean shareSessionWithRedis = false;
    private String cacheFilePath;
    private int kickoutMaxSession = 1;
    private boolean kickouAfter = false;

    public String getLogInOutResponseKey() {
        return logInOutResponseKey;
    }

    public void setLogInOutResponseKey(String logInOutResponseKey) {
        this.logInOutResponseKey = logInOutResponseKey;
    }

    public boolean isServiceOriented() {
        return serviceOriented;
    }

    public void setServiceOriented(boolean serviceOriented) {
        this.serviceOriented = serviceOriented;
    }

    public int getInvalidLoginCode() {
        return invalidLoginCode;
    }

    public void setInvalidLoginCode(int invalidLoginCode) {
        this.invalidLoginCode = invalidLoginCode;
    }

    public int getInvalidPermissionCode() {
        return invalidPermissionCode;
    }

    public void setInvalidPermissionCode(int invalidPermissionCode) {
        this.invalidPermissionCode = invalidPermissionCode;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public int getErrorTimes() {
        return errorTimes;
    }

    public void setErrorTimes(int errorTimes) {
        this.errorTimes = errorTimes;
    }

    public String getHashAlgorithmName() {
        return hashAlgorithmName;
    }

    public void setHashAlgorithmName(String hashAlgorithmName) {
        this.hashAlgorithmName = hashAlgorithmName;
    }

    public int getHashIterations() {
        return hashIterations;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public int getRetryLimit() {
        return retryLimit;
    }

    public void setRetryLimit(int retryLimit) {
        this.retryLimit = retryLimit;
    }

    public boolean isStoredCredentialsHexEncoded() {
        return storedCredentialsHexEncoded;
    }

    public void setStoredCredentialsHexEncoded(boolean storedCredentialsHexEncoded) {
        this.storedCredentialsHexEncoded = storedCredentialsHexEncoded;
    }

    public boolean isSwitchCacheToRedis() {
        return switchCacheToRedis;
    }

    public void setSwitchCacheToRedis(boolean switchCacheToRedis) {
        this.switchCacheToRedis = switchCacheToRedis;
    }

    public String getCacheFilePath() {
        return cacheFilePath;
    }

    public void setCacheFilePath(String cacheFilePath) {
        this.cacheFilePath = cacheFilePath;
    }

    public int getKickoutMaxSession() {
        return kickoutMaxSession;
    }

    public void setKickoutMaxSession(int kickoutMaxSession) {
        this.kickoutMaxSession = kickoutMaxSession;
    }

    public boolean isKickouAfter() {
        return kickouAfter;
    }

    public void setKickouAfter(boolean kickouAfter) {
        this.kickouAfter = kickouAfter;
    }

    public boolean isShareSessionWithRedis() {
        return shareSessionWithRedis;
    }

    public void setShareSessionWithRedis(boolean shareSessionWithRedis) {
        this.shareSessionWithRedis = shareSessionWithRedis;
    }
}
