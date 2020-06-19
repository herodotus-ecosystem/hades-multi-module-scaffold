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
 * File Name: EmpController.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.hr.controller.api;

import cn.com.felix.common.basic.controller.BaseController;
import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.core.utils.JacksonUtils;
import cn.com.felix.core.utils.datatables.DataTableResult;
import cn.com.felix.core.utils.datatables.DataTableUtils;
import cn.com.felix.hr.domain.Employee;
import cn.com.felix.hr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/hr/emp")
public class EmpController extends BaseController<Employee> {


    @Autowired
    private EmployeeService employeeService;

    @Override
    public String getPrefix() {
        return "/hr/emp";
    }

    @Override
    public BaseService<Employee, String> getBaseService() {
        return this.employeeService;
    }

    @Override
    public String getList(String aoData) {
        DataTableResult dataTableResult = DataTableUtils.parseDataTableParameter(aoData);

        List<Employee> pages = employeeService.queryData(dataTableResult);

        Map<String, Object> map = initPageResult(dataTableResult, dataTableResult.getTotal(), dataTableResult.getTotal() == 0 ? 0 : (int) (dataTableResult.getTotal() / pages.size()));
        map.put("data", pages);

        return JacksonUtils.toJson(map);
    }

    @RequestMapping(value = "/saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(Employee employee) {
        Employee result = employeeService.saveOrUpdate(employee);

        if (result != null) {
            return ajaxSuccess("保存成功！");
        } else {
            return ajaxError("保存失败！");
        }
    }
}
