/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: WxUserService
 * Author: hades
 * Date: 18-12-29 上午8:08
 * LastModified: 18-12-29 上午8:08
 */

package cn.com.felix.weapp.service;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.com.felix.core.enums.Gender;
import cn.com.felix.core.utils.encrypt.SecurityTools;
import cn.com.felix.weapp.domain.WxRequest;
import cn.com.felix.weapp.domain.WxUser;
import cn.com.felix.weapp.repository.WxUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2018/12/29
 */

@Service
@Transactional
public class WxUserService {

    private static final Logger logger = LoggerFactory.getLogger(WxUserService.class);

    private final WxUserRepository wxUserRepository;
    private final WxBasicService wxBasicService;

    @Autowired
    public WxUserService(WxUserRepository wxUserRepository, WxBasicService wxBasicService) {
        this.wxUserRepository = wxUserRepository;
        this.wxBasicService = wxBasicService;
    }

    private WxUser toWxUser(WxMaUserInfo wxMaUserInfo) {
        WxUser wxUser = new WxUser();
        wxUser.setOpenId(wxMaUserInfo.getOpenId());
        wxUser.setNickName(wxMaUserInfo.getNickName());
        wxUser.setGender(Gender.getGender(Integer.valueOf(wxMaUserInfo.getGender())));
        wxUser.setLanguage(wxMaUserInfo.getLanguage());
        wxUser.setCity(wxMaUserInfo.getCity());
        wxUser.setProvince(wxMaUserInfo.getProvince());
        wxUser.setCountry(wxMaUserInfo.getCountry());
        wxUser.setAvatarUrl(wxMaUserInfo.getAvatarUrl());
        wxUser.setUnionId(wxMaUserInfo.getUnionId());
        wxUser.setAppid(wxMaUserInfo.getWatermark().getAppid());

        return wxUser;
    }

    private WxMaUserInfo toWxMaUserInfo(WxUser wxUser) {
        WxMaUserInfo wxMaUserInfo = new WxMaUserInfo();
        wxMaUserInfo.setOpenId(wxUser.getOpenId());
        wxMaUserInfo.setNickName(wxUser.getNickName());
        wxMaUserInfo.setGender(String.valueOf(wxUser.getGender().getIndex()));
        wxMaUserInfo.setCity(wxUser.getCity());
        wxMaUserInfo.setProvince(wxUser.getProvince());
        wxMaUserInfo.setCountry(wxUser.getCountry());
        wxMaUserInfo.setAvatarUrl(wxUser.getAvatarUrl());
        wxMaUserInfo.setUnionId(wxUser.getUnionId());
        wxMaUserInfo.setLanguage(wxUser.getLanguage());
        WxMaUserInfo.Watermark wxWatermark = new WxMaUserInfo.Watermark();
        wxWatermark.setAppid(wxUser.getAppid());
        wxWatermark.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        wxMaUserInfo.setWatermark(wxWatermark);
        return wxMaUserInfo;
    }

    @Transactional
    public WxMaUserInfo storeWxUserInfo(WxRequest wxRequest, WxMaJscode2SessionResult wxSession) {

        WxUser wxUser = wxUserRepository.findByOpenId(wxSession.getOpenid());
        if (null == wxUser) {
            WxMaUserInfo wxUserInfo = wxBasicService.decryptWxUserInfo(wxSession.getSessionKey(), wxRequest);
            if (null != wxUserInfo) {
                wxUser = saveOrUpdate(wxUserInfo);
                if (logger.isDebugEnabled()) {
                    logger.debug("[Weapp] |- Fetch WX User Inforation: [{}]", wxUser.toString());
                }
            } else {
                logger.error("[Weapp] |- Decrypt User Info Error!");
                return new WxMaUserInfo();
            }
        }
        return toWxMaUserInfo(wxUser);
    }

    @Transactional
    public WxMaPhoneNumberInfo storePhoneNumberInfo(WxRequest wxRequest) {
        WxMaPhoneNumberInfo phoneNumberInfo = wxBasicService.decryptWxPhoneNumberInfo(wxRequest);
        if (null != phoneNumberInfo) {
            phoneNumberInfo.setPhoneNumber(SecurityTools.encrypt(phoneNumberInfo.getPhoneNumber()));
            phoneNumberInfo.setPurePhoneNumber(SecurityTools.encrypt(phoneNumberInfo.getPurePhoneNumber()));

            updateWxUserPhoneNumber(phoneNumberInfo, wxRequest.getOpenId());
            if (logger.isDebugEnabled()) {
                logger.debug("[Weapp] |- Fetch WX Phone Number Inforation: [{}]", phoneNumberInfo.toString());
            }
        } else {
            logger.error("[Weapp] |- Decrypt WX Phone Number Info Error!");
            return null;
        }

        return phoneNumberInfo;
    }

    @Transactional
    public WxUser updateWxUserPhoneNumber(WxMaPhoneNumberInfo wxMaPhoneNumberInfo, String openId) {
        WxUser wxUser = wxUserRepository.findByOpenId(openId);
        wxUser.setPhoneNumber(wxMaPhoneNumberInfo.getPhoneNumber());
        wxUser.setPurePhoneNumber(wxMaPhoneNumberInfo.getPurePhoneNumber());
        wxUser.setCountryCode(wxMaPhoneNumberInfo.getCountryCode());
        return wxUserRepository.save(wxUser);
    }

    @Transactional
    public WxUser saveOrUpdate(WxMaUserInfo wxMaUserInfo) {
        WxUser wxUser = wxUserRepository.save(toWxUser(wxMaUserInfo));
        if (logger.isDebugEnabled()) {
            logger.debug("[Weapp] |- Save WX User Success! ");
        }
        return wxUser;
    }
}
