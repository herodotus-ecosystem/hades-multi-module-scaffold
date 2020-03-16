package cn.com.felix.system.repository;

import cn.com.felix.system.domain.SysUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SysUserRepository extends JpaRepository<SysUser, String> {

    @EntityGraph(value = "SysUserWithRolesAndAuthority", type = EntityGraph.EntityGraphType.FETCH)
    SysUser findByUserName(String userName);

    @EntityGraph(value = "SysUserWithRolesAndAuthority", type = EntityGraph.EntityGraphType.FETCH)
    SysUser findByUid(String id);

    @Modifying
    @Transactional
    @Query("delete from SysUser su where su.uid = ?1")
    void deleteByUid(String id);

    SysUser findByOpenId(String openid);

    SysUser findByEmployeeId(String employeeid);
}
