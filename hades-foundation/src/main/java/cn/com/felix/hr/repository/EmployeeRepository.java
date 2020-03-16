/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: EmployeeRepository
 * Author: gengwei.zheng
 * Date: 18-12-29 上午8:50
 * LastModified: 18-12-25 下午5:25
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
