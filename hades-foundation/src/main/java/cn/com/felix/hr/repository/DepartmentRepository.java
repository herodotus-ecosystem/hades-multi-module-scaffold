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
 * File Name: DepartmentRepository.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.hr.repository;

import cn.com.felix.hr.domain.Department;
import cn.com.felix.hr.dto.DeptVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/2/14
 */
public interface DepartmentRepository extends JpaRepository<Department, String> {

    Department findByPkid(String pkid);

    List<Department> findAllByOrgnizationIdOrderByRankingAsc(String orgnizationid);

    @Modifying
    @Transactional
    @Query("delete from Department e where e.pkid = ?1")
    void deleteByPkid(String pkid);

    @Query(value = "SELECT new cn.com.felix.hr.dto.DeptVo(dept.pkid,dept.createTime,dept.departmentName,dept.orgnizationId,dept.parentId,dept.ranking,dept.updateTime,org.orgnaizationName,pdept.departmentName AS parentDepartmentName) from Department dept left join Orgnization  org on dept.orgnizationId=org.pkid left join Department pdept on dept.parentId = pdept.pkid",
            countQuery = "select count(*) from Department")
    Page<DeptVo> findDeptAndOrg(Pageable pageable);


    @Query(value = "SELECT new cn.com.felix.hr.dto.DeptVo(dept.pkid,dept.createTime,dept.departmentName,dept.orgnizationId,dept.parentId,dept.ranking,dept.updateTime,org.orgnaizationName,pdept.departmentName AS parentDepartmentName) from Department dept left join Orgnization  org on dept.orgnizationId=org.pkid left join Department pdept on dept.parentId = pdept.pkid where dept.orgnizationId = ?1 order by org.ranking,pdept.ranking,dept.ranking")
    List<DeptVo> findDeptAndOrgByOrgID(String orgId);

//    @Query(value = "select new pers.zpw.domain.CityHohel(t1.name AS cityName,t2.name AS hotelName) from  TCity t1 left  join THotel t2 on t1.id=t2.city where t2.name =:name",
//            countQuery = "select count(*) from  TCity t1 left  join THotel t2 on t1.id=t2.city where t2.name =:name")
//    Page<CityHohel> findCityAndHotelAllSelf(@Param("name") String name, Pageable pageable);
}
