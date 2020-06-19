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
 * File Name: SysWeappService.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.system.service;

import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.system.domain.SysWeapp;
import cn.com.felix.system.repository.SysWeappRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysWeappService extends BaseService<SysWeapp, String> {

    private final SysWeappRepository sysWeappRepository;

    @Autowired
    public SysWeappService(SysWeappRepository sysWeappRepository) {
        this.sysWeappRepository = sysWeappRepository;
    }

    @Override
    public SysWeapp findById(String wid) {
        return sysWeappRepository.findByWid(wid);
    }

    @Override
    public void deleteById(String wid) {
        sysWeappRepository.deleteByWid(wid);
    }

    @Override
    public Page<SysWeapp> findByPage(int pageNumber, int pageSize) {
        return sysWeappRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "wid"));
    }

    public List<SysWeapp> findAll() {
        return sysWeappRepository.findAllByAvailable(true);
    }

    public SysWeapp saveOrUpdate(SysWeapp sysWeapp) {
        return sysWeappRepository.save(sysWeapp);
    }

}
