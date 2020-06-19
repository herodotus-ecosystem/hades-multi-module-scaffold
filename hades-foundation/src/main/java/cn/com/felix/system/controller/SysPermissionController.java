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
 * File Name: SysPermissionController.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.system.controller;

import cn.com.felix.common.basic.controller.BaseController;
import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.dto.SysPermissionDTO;
import cn.com.felix.system.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/system/permission")
public class SysPermissionController extends BaseController<SysPermission> {

    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public String getPrefix() {
        return "/system/permission";
    }

    @Override
    public BaseService<SysPermission, String> getBaseService() {
        return this.sysPermissionService;
    }

    @ModelAttribute(value = "permissions")
    public List<SysPermission> getAllPermissions() {
        return sysPermissionService.findAll();
    }

    @RequestMapping(value = "/saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(SysPermissionDTO sysPermissionDTO) {

        SysPermission result = sysPermissionService.saveOrUpdate(sysPermissionDTO);
        if (result != null) {
            return ajaxSuccess("保存成功！");
        } else {
            return ajaxError("保存失败！");
        }
    }
}
