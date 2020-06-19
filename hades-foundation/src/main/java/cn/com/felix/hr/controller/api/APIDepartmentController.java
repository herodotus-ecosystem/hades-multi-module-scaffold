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
 * File Name: APIDepartmentController.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.hr.controller.api;

import cn.com.felix.core.enums.ResultEnum;
import cn.com.felix.core.extend.json.Result;
import cn.com.felix.hr.domain.Department;
import cn.com.felix.hr.dto.EmployeeEssentialDTO;
import cn.com.felix.hr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/2/14
 */

@RestController
@RequestMapping(value = "/api/wx/department")
public class APIDepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public APIDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/list")
    public Result departmentList(String orgnizationid) {

        List<Department> departments = departmentService.findAllByOrgnizationId(orgnizationid);

        Result result = new Result();

        if (null != departments) {
            result.put("departments", departments);
            result.setStatus(ResultEnum.OK.getStatus());
        } else {
            result.setStatus(ResultEnum.UNAUTHORIZED.getStatus());
            result.setMessage("系统出错！");
        }

        return result;
    }
}
