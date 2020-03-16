package cn.com.felix.system.repository;

import cn.com.felix.system.domain.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SysLogRepository extends JpaRepository<SysLog, String> {
}
