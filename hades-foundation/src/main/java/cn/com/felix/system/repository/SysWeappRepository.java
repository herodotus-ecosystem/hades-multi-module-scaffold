package cn.com.felix.system.repository;

import cn.com.felix.core.enums.StateEnum;
import cn.com.felix.system.domain.SysWeapp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysWeappRepository extends JpaRepository<SysWeapp, String> {

    @Modifying
    @Transactional
    @Query("delete from SysWeapp sw where sw.wid = ?1")
    void deleteByWid(String wid);

    SysWeapp findByWid(String wid);

    List<SysWeapp> findAllByAvailable(boolean available);
}
