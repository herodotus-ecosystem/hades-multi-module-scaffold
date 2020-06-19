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
 * File Name: EmployeeEssentialDTO.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
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
