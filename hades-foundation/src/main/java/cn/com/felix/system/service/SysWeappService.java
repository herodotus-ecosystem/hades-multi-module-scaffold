package cn.com.felix.system.service;

import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.system.domain.SysWeapp;
import cn.com.felix.system.repository.SysWeappRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysWeappService extends BaseService<SysWeapp, String> {

    private final SysWeappRepository sysWeappRepository;

    @Autowired
    public SysWeappService(SysWeappRepository sysWeappRepository) {
        this.sysWeappRepository = sysWeappRepository;
    }

    @Override
    public SysWeapp findById(String wid) {
        return sysWeappRepository.findByWid(wid);
    }

    @Override
    public void deleteById(String wid) {
        sysWeappRepository.deleteByWid(wid);
    }

    @Override
    public Page<SysWeapp> findByPage(int pageNumber, int pageSize) {
        return sysWeappRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "wid"));
    }

    public List<SysWeapp> findAll() {
        return sysWeappRepository.findAllByAvailable(true);
    }

    public SysWeapp saveOrUpdate(SysWeapp sysWeapp) {
        return sysWeappRepository.save(sysWeapp);
    }

}
