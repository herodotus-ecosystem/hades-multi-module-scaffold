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
 * File Name: SysLogService.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.system.service;

import cn.com.felix.core.utils.DateUtils;
import cn.com.felix.system.domain.SysLog;
import cn.com.felix.system.domain.SysUser;
import cn.com.felix.system.repository.SysLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogService {

    @Autowired
    private SysLogRepository sysLogRepository;

    public void record(SysUser sysUser, String ip, String action) {

        SysLog sysLog = new SysLog();
        sysLog.setUid(sysUser.getUid());
        sysLog.setUserName(sysUser.getUserName());
        sysLog.setLoginTime(DateUtils.getCurrentTime());
        sysLog.setLoginIp(ip);
        sysLog.setLoginAction(action);

        sysLogRepository.save(sysLog);
    }
}
