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
 * File Name: DepartmentService.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.hr.service;

import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.hr.domain.Department;
import cn.com.felix.hr.dto.DeptVo;
import cn.com.felix.hr.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/2/14
 */

@Service
public class DepartmentService extends BaseService<Department,String> {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAllByOrgnizationId(String orgnizationid) {
        return departmentRepository.findAllByOrgnizationIdOrderByRankingAsc(orgnizationid);
    }

    @Override
    public Department findById(String id) {
        return this.departmentRepository.findByPkid(id);
    }

    @Override
    public void deleteById(String pkid) {
        departmentRepository.deleteByPkid(pkid);
    }

    @Override
    public Page<Department> findByPage(int pageNumber, int pageSize) {
        return departmentRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "ranking"));
    }

    public Page<DeptVo> findByPageVo(int pageNumber, int pageSize) {
        //new PageRequest(1,20)
        return departmentRepository.findDeptAndOrg(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC,"orgnizationId" ,"parentId","ranking"));
    }



    public Department saveOrUpdate(Department department) {
        return departmentRepository.save(department);
    }

    public List<DeptVo> findAllByOrgID(String org) {
        return departmentRepository.findDeptAndOrgByOrgID(org);
    }
}
