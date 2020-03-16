/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: APIDepartmentController
 * Author: gengwei.zheng
 * Date: 19-2-14 下午4:02
 * LastModified: 19-2-14 下午4:02
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
 * @author gengwei.zheng
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
