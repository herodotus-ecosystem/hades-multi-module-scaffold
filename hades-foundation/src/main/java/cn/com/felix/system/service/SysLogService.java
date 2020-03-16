package cn.com.felix.system.service;

import cn.com.felix.core.utils.DateUtils;
import cn.com.felix.system.domain.SysLog;
import cn.com.felix.system.domain.SysUser;
import cn.com.felix.system.repository.SysLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogService {

    @Autowired
    private SysLogRepository sysLogRepository;

    public void record(SysUser sysUser, String ip, String action) {

        SysLog sysLog = new SysLog();
        sysLog.setUid(sysUser.getUid());
        sysLog.setUserName(sysUser.getUserName());
        sysLog.setLoginTime(DateUtils.getCurrentTime());
        sysLog.setLoginIp(ip);
        sysLog.setLoginAction(action);

        sysLogRepository.save(sysLog);
    }
}
