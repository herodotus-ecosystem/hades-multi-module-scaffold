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
 * Module Name: hades-foundation
 * File Name: SysWeappDefaultRoleService.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.system.service;

import cn.com.felix.system.domain.SysWeappDefaultRole;
import cn.com.felix.system.repository.SysWeappDefaultRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Description: </p>
 *
 * @author hades
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
