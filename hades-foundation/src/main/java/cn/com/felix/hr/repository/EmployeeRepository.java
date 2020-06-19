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
 * File Name: EmployeeRepository.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.hr.repository;

import cn.com.felix.core.enums.Identity;
import cn.com.felix.hr.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Employee findByPhoneNumber(String phoneNumber);

    Employee findByPkid(String employeeid);

    @Modifying
    @Transactional
    @Query("delete from Employee e where e.pkid = ?1")
    void deleteByPkid(String id);

    @EntityGraph(value = "Employee.Graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Employee> findAllByOrgnizationIdAndIdentityOrderByRankingAsc(String orgnizationId, Identity identity);

}
