/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: EmployeeController
 * Author: hades
 * Date: 18-12-29 上午10:18
 * LastModified: 18-12-29 上午10:18
 */

package cn.com.felix.hr.controller.api;

import cn.com.felix.core.enums.Identity;
import cn.com.felix.core.enums.ResultEnum;
import cn.com.felix.core.extend.json.Result;
import cn.com.felix.hr.domain.Employee;
import cn.com.felix.hr.dto.DepartmentEmployeeDTO;
import cn.com.felix.hr.dto.EmployeeEssentialDTO;
import cn.com.felix.hr.service.EmployeeService;
import cn.com.felix.weapp.dto.component.WuxSelectOptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2018/12/29
 */
@RestController
@RequestMapping(value = "/api/wx/employee")
public class APIEmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public APIEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 前端 给一个 openid  就是每个微信对应小程序的唯一ID。
     * 系统 去查sys user 有没有这个openid就是查有没有关联
     * @param openId
     * @return
     */
    @PostMapping("/employeeInfo")
    public Result essentialInformation(String openId) {

        EmployeeEssentialDTO employeeEssentialDTO = employeeService.essentialInformation(openId);

        Result result = new Result();

        if (null != employeeEssentialDTO) {
            result.put("employeeInfo", employeeEssentialDTO);
            result.setStatus(ResultEnum.OK.getStatus());
        } else {
            result.setStatus(ResultEnum.UNAUTHORIZED.getStatus());
            result.setMessage("请升级到最新微信版本后重试!");
        }

        return result;
    }

    @PostMapping("/getAllLeadership")
    public Result getAllLeadership(String orgnizationid) {

        List<Employee> leaderships = employeeService.findAllByOrgnizationIdAndIdentity(orgnizationid, Identity.LEADERSHIP);

        Result result = new Result();

        if (null != leaderships) {

            List<WuxSelectOptionDTO> wuxSelectOptionDTOList = new ArrayList<>();

            for (Employee employee : leaderships) {
                WuxSelectOptionDTO  wuxSelectOptionDTO = new WuxSelectOptionDTO();
                wuxSelectOptionDTO.setTitle(employee.getFullName());
                wuxSelectOptionDTO.setValue(employee.getPkid());
                wuxSelectOptionDTO.setRanking(employee.getRanking());
                wuxSelectOptionDTOList.add(wuxSelectOptionDTOList.size(), wuxSelectOptionDTO);
            }

            result.put("leaderships", wuxSelectOptionDTOList);
            result.setStatus(ResultEnum.OK.getStatus());
        } else {
            result.setStatus(ResultEnum.UNAUTHORIZED.getStatus());
            result.setMessage("系统出错！");
        }

        return result;
    }

    /**
     * 根据组织 ID，返回该组织下所有员工，换部门分组
     * @param orgnizationid
     * @return
     */
    @PostMapping("/getEmployeeGroup")
    public Result getEmployeeGroup(String orgnizationid) {

        List<Employee> employees = employeeService.findAllByOrgnizationIdAndIdentity(orgnizationid, Identity.STAFF);

        Result result = new Result();

        if (null != employees) {
            DepartmentEmployeeDTO departmentEmployeeDTO = new DepartmentEmployeeDTO(employees);
            result.put("employeeGroup", departmentEmployeeDTO.getDatas());
            result.setStatus(ResultEnum.OK.getStatus());
        } else {
            result.setStatus(ResultEnum.UNAUTHORIZED.getStatus());
            result.setMessage("系统出错！");
        }

        return result;
    }
}
