/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: WxSecurityService
 * Author: gengwei.zheng
 * Date: 18-12-29 上午8:28
 * LastModified: 18-12-29 上午8:28
 */

package cn.com.felix.weapp.service;

import cn.com.felix.system.domain.SysUser;
import cn.com.felix.system.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
 * @date 2018/12/29
 */
@Service
@Transactional
public class WxSecurityService {

    private static final Logger logger = LoggerFactory.getLogger(WxSecurityService.class);

    private final SysUserService sysUserService;

    @Autowired
    public WxSecurityService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Transactional
    public boolean isWxUserBinding(String openId) {
        SysUser sysUser = sysUserService.findByOpenId(openId);
        if (null == sysUser) {
            return false;
        }
        return true;
    }

}
