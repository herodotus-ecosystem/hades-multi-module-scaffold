/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: DepartmentService
 * Author: gengwei.zheng
 * Date: 19-2-14 下午4:00
 * LastModified: 19-2-14 下午4:00
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
 * @author gengwei.zheng
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
