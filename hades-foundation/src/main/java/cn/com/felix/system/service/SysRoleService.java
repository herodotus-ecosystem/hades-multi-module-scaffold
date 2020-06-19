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
 * File Name: SysRoleService.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.system.service;

import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.common.config.shiro.DynamicShiroFilterFactoryBean;
import cn.com.felix.core.enums.StateEnum;
import cn.com.felix.hr.domain.Employee;
import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.domain.SysRole;
import cn.com.felix.system.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SysRoleService extends BaseService<SysRole, String> {

    private final SysRoleRepository sysRoleRepository;
    @Autowired
    private DynamicShiroFilterFactoryBean dynamicShiroFilterFactoryBean;

    @Autowired
    public SysRoleService(SysRoleRepository sysRoleRepository) {
        this.sysRoleRepository = sysRoleRepository;
    }

    @Override
    public SysRole findById(String id) {
        return sysRoleRepository.findByRid(id);
    }

    @Override
    public void deleteById(String id) {
        sysRoleRepository.deleteByRid(id);
    }

    @Override
    public Page<SysRole> findByPage(int pageNumber, int pageSize) {
        return sysRoleRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "rid"));
    }

    public void updateRolePermissions(String[] permissions, String roleId) {

        Set<SysPermission> sysPermissions = new HashSet<>();
        for (String permission : permissions) {
            SysPermission sysPermission = new SysPermission();
            sysPermission.setPid(permission);
            sysPermissions.add(sysPermission);
        }

        SysRole sysRole = findByRid(roleId);
        sysRole.setPermissions(sysPermissions);

        sysRoleRepository.saveAndFlush(sysRole);
        dynamicShiroFilterFactoryBean.updateUserPermission();
    }

    public List<SysRole> findByAppidAndState(String appid, StateEnum stateEnum) {
        return sysRoleRepository.findByAppidAndState(appid, stateEnum);
    }

    public SysRole saveOrUpdate(SysRole sysRole) {
        return sysRoleRepository.save(sysRole);
    }

    public List<SysRole> findByAppidAndRoleName(String appid, String roleName) {
        return sysRoleRepository.findByAppidAndRoleName(appid, roleName);
    }

    private List<Employee> change(List<Object[]> tmp) {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        for (Object[] a : tmp) {
            employee = new Employee();
            employee.setPkid((String) a[1]);
            employee.setFullName((String) a[0]);
            employee.setPhoneNumber((String) a[2]);
            employees.add(employee);
        }
        return employees;
    }

    public List<Employee> findUserAllNative(String roleName) {
        List<Object[]> tmp = sysRoleRepository.findUserAllNative(roleName);
        return change(tmp);
    }

    public List<Employee> findUserAllNative(String roleName, String deptId) {
        List<Object[]> tmp = sysRoleRepository.findUserAllNative(roleName, deptId);
        return change(tmp);
    }


    public SysRole findByRid(String id) {
        return sysRoleRepository.findByRid(id);
    }

    public void delete(String id) {
        sysRoleRepository.deleteById(id);
    }

    public List<SysRole> findAll() {
        return sysRoleRepository.findAll();
    }

    public List<SysRole> getRoles() {
        return sysRoleRepository.findAll();
    }

}
