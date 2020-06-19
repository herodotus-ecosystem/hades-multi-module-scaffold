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
 * File Name: SysPermissionService.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.system.service;

import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.common.config.shiro.DynamicShiroFilterFactoryBean;
import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.domain.SysRole;
import cn.com.felix.system.dto.SysPermissionDTO;
import cn.com.felix.system.dto.SysPermissionTreeDTO;
import cn.com.felix.system.repository.SysPermissionRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SysPermissionService extends BaseService<SysPermission, String> {

    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    @Resource
    private DynamicShiroFilterFactoryBean dynamicShiroFilterFactoryBean;

    public List<SysPermission> findAll() {
        return sysPermissionRepository.findAll();
    }

    private SysPermission toDomain(SysPermissionDTO dto) {
        SysPermission sysPermission = new SysPermission();
        sysPermission.setPid(dto.getPid());
        sysPermission.setPermissionName(dto.getPermissionName());
        sysPermission.setPermission(dto.getPermission());
        sysPermission.setMenuClass(dto.getMenuClass());
        sysPermission.setResourceType(dto.getResourceType());
        sysPermission.setUrl(dto.getUrl());

        if (StringUtils.isNotEmpty(dto.getParentId())) {
            SysPermission parent = new SysPermission();
            parent.setPid(dto.getParentId());
            sysPermission.setParent(parent);
        }

        return sysPermission;
    }

    @Override
    public SysPermission findById(String id) {
        return sysPermissionRepository.findByPid(id);
    }

    @Override
    public void deleteById(String id) {
        sysPermissionRepository.deleteByPid(id);
        dynamicShiroFilterFactoryBean.updateAllPermission();
    }

    @Override
    public Page<SysPermission> findByPage(int pageNumber, int pageSize) {
        return sysPermissionRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "pid"));
    }

    public SysPermission saveOrUpdate(SysPermissionDTO sysPermissionDTO) {
        SysPermission sysPermission = sysPermissionRepository.save(toDomain(sysPermissionDTO));
        if (ObjectUtils.isNotEmpty(sysPermission)) {
            dynamicShiroFilterFactoryBean.updateAllPermission();
        }
        return sysPermission;
    }

    public List<SysPermissionTreeDTO> findPermissionTreeByRoleId(String roleId) {
        List<SysPermission> sysPermissions = findAll();
        List<SysPermissionTreeDTO> sysPermissionTreeDTOS = new ArrayList<>();

        for (SysPermission sysPermission : sysPermissions) {
            SysPermissionTreeDTO sysPermissionTreeDTO = new SysPermissionTreeDTO();
            sysPermissionTreeDTO.setId(sysPermission.getPid());
            sysPermissionTreeDTO.setpId(sysPermission.getParent() == null ? "0" : sysPermission.getParent().getPid());
            sysPermissionTreeDTO.setName(sysPermission.getPermissionName());

            if (CollectionUtils.isNotEmpty(sysPermission.getRoles())) {
                for (SysRole sysRole : sysPermission.getRoles()) {
                    if (sysRole.getRid().equals(roleId)) {
                        sysPermissionTreeDTO.setChecked(true);
                        break;
                    }
                }
            }
            sysPermissionTreeDTOS.add(sysPermissionTreeDTO);
        }

        return sysPermissionTreeDTOS;
    }
}
