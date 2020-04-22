/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: EmployeeService
 * Author: hades
 * Date: 18-12-29 上午8:50
 * LastModified: 18-12-25 下午5:13
 */

package cn.com.felix.hr.service;

import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.hr.domain.Orgnization;
import cn.com.felix.hr.repository.OrgnizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgnizationService extends BaseService<Orgnization, String> {

    private final OrgnizationRepository orgnizationRepository;

    @Autowired
    public OrgnizationService(OrgnizationRepository orgnizationRepository) {
        this.orgnizationRepository = orgnizationRepository;
    }


    @Override
    public Orgnization findById(String pkid) {
        return orgnizationRepository.findByPkid(pkid);
    }

    @Override
    public void deleteById(String pkid) {
        orgnizationRepository.deleteByPkid(pkid);
    }

    @Override
    public Page<Orgnization> findByPage(int pageNumber, int pageSize) {
        return orgnizationRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "ranking"));
    }

    public Orgnization saveOrUpdate(Orgnization orgnization) {
        return orgnizationRepository.save(orgnization);
    }

    public List<Orgnization> findAll() {
        return orgnizationRepository.findAll(new Sort(Sort.Direction.ASC, "ranking"));
    }
}
