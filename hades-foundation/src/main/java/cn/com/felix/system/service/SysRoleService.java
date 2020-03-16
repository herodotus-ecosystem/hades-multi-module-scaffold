package cn.com.felix.system.service;

import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.common.config.shiro.DynamicShiroFilterFactoryBean;
import cn.com.felix.core.enums.StateEnum;
import cn.com.felix.hr.domain.Employee;
import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.domain.SysRole;
import cn.com.felix.system.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SysRoleService extends BaseService<SysRole, String> {

    private final SysRoleRepository sysRoleRepository;
    @Autowired
    private DynamicShiroFilterFactoryBean dynamicShiroFilterFactoryBean;

    @Autowired
    public SysRoleService(SysRoleRepository sysRoleRepository) {
        this.sysRoleRepository = sysRoleRepository;
    }

    @Override
    public SysRole findById(String id) {
        return sysRoleRepository.findByRid(id);
    }

    @Override
    public void deleteById(String id) {
        sysRoleRepository.deleteByRid(id);
    }

    @Override
    public Page<SysRole> findByPage(int pageNumber, int pageSize) {
        return sysRoleRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "rid"));
    }

    public void updateRolePermissions(String[] permissions, String roleId) {

        Set<SysPermission> sysPermissions = new HashSet<>();
        for (String permission : permissions) {
            SysPermission sysPermission = new SysPermission();
            sysPermission.setPid(permission);
            sysPermissions.add(sysPermission);
        }

        SysRole sysRole = findByRid(roleId);
        sysRole.setPermissions(sysPermissions);

        sysRoleRepository.saveAndFlush(sysRole);
        dynamicShiroFilterFactoryBean.updateUserPermission();
    }

    public List<SysRole> findByAppidAndState(String appid, StateEnum stateEnum) {
        return sysRoleRepository.findByAppidAndState(appid, stateEnum);
    }

    public SysRole saveOrUpdate(SysRole sysRole) {
        return sysRoleRepository.save(sysRole);
    }

    public List<SysRole> findByAppidAndRoleName(String appid, String roleName) {
        return sysRoleRepository.findByAppidAndRoleName(appid, roleName);
    }

    private List<Employee> change(List<Object[]> tmp) {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        for (Object[] a : tmp) {
            employee = new Employee();
            employee.setPkid((String) a[1]);
            employee.setFullName((String) a[0]);
            employee.setPhoneNumber((String) a[2]);
            employees.add(employee);
        }
        return employees;
    }

    public List<Employee> findUserAllNative(String roleName) {
        List<Object[]> tmp = sysRoleRepository.findUserAllNative(roleName);
        return change(tmp);
    }

    public List<Employee> findUserAllNative(String roleName, String deptId) {
        List<Object[]> tmp = sysRoleRepository.findUserAllNative(roleName, deptId);
        return change(tmp);
    }


    public SysRole findByRid(String id) {
        return sysRoleRepository.findByRid(id);
    }

    public void delete(String id) {
        sysRoleRepository.deleteById(id);
    }

    public List<SysRole> findAll() {
        return sysRoleRepository.findAll();
    }

    public List<SysRole> getRoles() {
        return sysRoleRepository.findAll();
    }

}
