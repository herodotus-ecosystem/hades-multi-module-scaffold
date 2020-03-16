/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: DepartmentRepository
 * Author: gengwei.zheng
 * Date: 19-2-14 下午3:59
 * LastModified: 19-2-14 下午3:59
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
 * @author gengwei.zheng
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
