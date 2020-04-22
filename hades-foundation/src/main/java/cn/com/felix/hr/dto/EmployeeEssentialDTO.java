/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: WxUserDTO
 * Author: hades
 * Date: 18-12-29 上午10:20
 * LastModified: 18-12-25 下午5:23
 */

package cn.com.felix.hr.dto;

import cn.com.felix.common.basic.dto.BaseDTO;
import cn.com.felix.hr.domain.Department;
import cn.com.felix.hr.domain.Employee;
import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.domain.SysRole;
import cn.com.felix.system.domain.SysUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeEssentialDTO extends BaseDTO {

    private Employee employee;
    private Department department;
    private List<SysRole> sysRole = new ArrayList<>();
    private List<SysPermission> sysPermissions = new ArrayList<>();

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        setDepartment(employee.getDepartment());
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<SysRole> getSysRole() {
        return sysRole;
    }

    public void setSysRole(List<SysRole> sysRole) {
        this.sysRole = sysRole;
    }

    //    public SysRole getSysRole() {
//        return sysRole;
//    }
//
//    public void setSysRole(SysRole sysRole) {
//        this.sysRole = sysRole;
//    }

    public void setSysRoles(SysUser sysUser) {
        if (null != sysUser) {
            Map<String, SysPermission> map = new HashMap<>();

            if (null != sysUser) {
                for (SysRole sysRole : sysUser.getRoles()) {

                    //setSysRole(sysRole);
                    this.sysRole.add(sysRole);

                    for (SysPermission sysPermission : sysRole.getPermissions()) {
                        if (!map.containsKey(sysPermission.getPid())) {
                            if (null != sysPermission.getParent()) {
                                map.put(sysPermission.getPid(), sysPermission);
                            }
                        }
                    }
                }
            }

            setSysPermissions(new ArrayList<>(map.values()));
        }
    }

    public List<SysPermission> getSysPermissions() {
        return sysPermissions;
    }

    public void setSysPermissions(List<SysPermission> sysPermissions) {
        this.sysPermissions = sysPermissions;
    }
}
