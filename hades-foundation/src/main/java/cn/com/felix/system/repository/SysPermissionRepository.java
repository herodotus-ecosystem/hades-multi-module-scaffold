package cn.com.felix.system.repository;

import cn.com.felix.system.domain.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SysPermissionRepository extends JpaRepository<SysPermission, String> {

    SysPermission findByPid(String id);

    @Modifying
    @Transactional
    @Query("delete from SysPermission sp where sp.pid = ?1")
    void deleteByPid(String id);
}
