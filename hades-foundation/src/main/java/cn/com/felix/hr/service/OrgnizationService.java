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
 * File Name: OrgnizationService.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
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
