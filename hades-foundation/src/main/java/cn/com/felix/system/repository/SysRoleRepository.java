package cn.com.felix.system.repository;

import cn.com.felix.core.enums.StateEnum;
import cn.com.felix.hr.domain.Employee;
import cn.com.felix.system.domain.SysRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SysRoleRepository extends JpaRepository<SysRole, String> {

    @EntityGraph(value = "SysRoleWithAndAuthority", type = EntityGraph.EntityGraphType.FETCH)
    SysRole findByRid(String id);


    @Modifying
    @Transactional
    @Query("delete from SysRole sr where sr.rid = ?1")
    void deleteByRid(String id);

    List<SysRole> findByAppidAndState(String appid, StateEnum stateEnum);

    List<SysRole> findByAppid(String appid);

    List<SysRole> findByAppidAndRoleName(String appid, String roleName);

    @Query(value = "SELECT se.full_name,se.pkid,se.phone_number FROM sys_user_role ur LEFT JOIN sys_role r on ur.rid=r.rid " +
            "LEFT JOIN sys_user su on ur.uid = su.uid " +
            "LEFT JOIN sys_employee se on su.employeeid =se.pkid " +
            "WHERE r.role_name=:roleName AND se.departmentid=:deptId", nativeQuery = true)
    List<Object[]> findUserAllNative(@Param("roleName") String roleName, @Param("deptId") String deptId);


    @Query(value = "SELECT se.full_name,se.pkid,se.phone_number FROM sys_user_role ur LEFT JOIN sys_role r on ur.rid=r.rid " +
            "LEFT JOIN sys_user su on ur.uid = su.uid " +
            "LEFT JOIN sys_employee se on su.employeeid =se.pkid " +
            "WHERE r.role_name=:roleName ", nativeQuery = true)
    List<Object[]> findUserAllNative(@Param("roleName") String roleName);
}