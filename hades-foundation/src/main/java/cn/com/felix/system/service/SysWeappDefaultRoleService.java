/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: SysWeappDefaultRoleService
 * Author: gengwei.zheng
 * Date: 19-3-11 下午3:12
 * LastModified: 19-3-11 下午3:12
 */

package cn.com.felix.system.service;

import cn.com.felix.system.domain.SysWeappDefaultRole;
import cn.com.felix.system.repository.SysWeappDefaultRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
 * @date 2019/3/11
 */
@Service
public class SysWeappDefaultRoleService {

    private final SysWeappDefaultRoleRepository sysWeappDefaultRoleRepository;

    @Autowired
    public SysWeappDefaultRoleService(SysWeappDefaultRoleRepository sysWeappDefaultRoleRepository) {
        this.sysWeappDefaultRoleRepository = sysWeappDefaultRoleRepository;
    }

    public SysWeappDefaultRole findByAppidAndOrgnizationid(String appid, String orgnizationid) {
        return sysWeappDefaultRoleRepository.findByAppidAndOrgnizationid(appid, orgnizationid);
    }
}
