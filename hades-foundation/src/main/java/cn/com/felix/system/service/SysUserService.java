package cn.com.felix.system.service;

import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.common.config.shiro.CurrentUser;
import cn.com.felix.common.config.shiro.DynamicShiroFilterFactoryBean;
import cn.com.felix.common.config.shiro.PasswordHelper;
import cn.com.felix.core.enums.StateEnum;
import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.domain.SysRole;
import cn.com.felix.system.domain.SysUser;
import cn.com.felix.system.domain.SysWeappDefaultRole;
import cn.com.felix.system.repository.SysUserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@CacheDefaults(cacheName = "SysUser")
public class SysUserService extends BaseService<SysUser, String> {

    private static final Logger logger = LoggerFactory.getLogger(SysUserService.class);

    private final PasswordHelper passwordHelper;
    private final SysUserRepository sysUserRepository;
    private final SysRoleService sysRoleService;
    private final SysWeappDefaultRoleService sysWeappDefaultRoleService;

    @Autowired
    private DynamicShiroFilterFactoryBean dynamicShiroFilterFactoryBean;

    @Autowired
    public SysUserService(SysUserRepository sysUserRepository, SysRoleService sysRoleService, PasswordHelper passwordHelper, SysWeappDefaultRoleService sysWeappDefaultRoleService) {
        this.sysUserRepository = sysUserRepository;
        this.sysRoleService = sysRoleService;
        this.passwordHelper = passwordHelper;
        this.sysWeappDefaultRoleService = sysWeappDefaultRoleService;
    }

    @CacheResult
    public SysUser findByUserName(String userName) {
        if (logger.isTraceEnabled()) {
            logger.trace("[SysUser Service] |- findByUserName: [{}]", userName);
        }

        return sysUserRepository.findByUserName(userName);
    }

    @CacheResult
    public SysUser findByOpenId(String openId) {
        return sysUserRepository.findByOpenId(openId);
    }

    @CacheResult
    @Override
    public SysUser findById(String id) {
        return sysUserRepository.findByUid(id);
    }

    @CacheRemove
    @Override
    public void deleteById(String id) {
        sysUserRepository.deleteByUid(id);
    }

    @Override
    public Page<SysUser> findByPage(int pageNumber, int pageSize) {
        return sysUserRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "uid"));
    }

    @CachePut
    public SysUser saveOrUpdate(SysUser sysUser) {
        return sysUserRepository.save(sysUser);
    }

    public List<SysUser> batchSaveOrUpdate(List<SysUser> sysUsers) {
        return sysUserRepository.saveAll(sysUsers);
    }

    public SysUser findByEmployeeId(String employeeid) {
        return sysUserRepository.findByEmployeeId(employeeid);
    }

    @Transactional
    public SysUser createUser(String userName, String employeeid, String orgnizationid, String openId, String appid) {

        SysUser sysUser = new SysUser();
        sysUser.setUserName(userName);
        sysUser.setOpenId(openId);
        sysUser.setEmployeeId(employeeid);
        sysUser = passwordHelper.encryptPassword(sysUser, openId);

        SysWeappDefaultRole weappDefaultRole = sysWeappDefaultRoleService.findByAppidAndOrgnizationid(appid, orgnizationid);
        Set<SysRole> roles = new HashSet<>();
        roles.add(weappDefaultRole.getRole());
        sysUser.setRoles(roles);

        return saveOrUpdate(sysUser);
    }

    @Transactional
    public List<SysUser> changeUserRole(String selectEmployeeId, String roleName, String appid) {

        SysUser selectSysUser = findByEmployeeId(selectEmployeeId);

        List<SysRole> sysRoles = sysRoleService.findByAppidAndRoleName(appid, roleName);

        if (CollectionUtils.isNotEmpty(sysRoles)) {

            List<SysUser> result = new ArrayList<>();
            Set<SysRole> selectSysUserRoles = new HashSet<>(sysRoles);
            selectSysUser.setRoles(selectSysUserRoles);
            result.add(selectSysUser);

            for (SysRole sysRole : sysRoles) {
                Set<SysUser> oldReceivers = sysRole.getUsers();

                if (CollectionUtils.isNotEmpty(oldReceivers)) {
                    List<SysRole> defaultRole = sysRoleService.findByAppidAndState(appid, StateEnum.DEFAULT);
                    for (SysUser oldReceiver : oldReceivers) {
                        Set<SysRole> defaultRoleSet = new HashSet<>();
                        defaultRoleSet.addAll(defaultRole);
                        oldReceiver.setRoles(defaultRoleSet);
                        result.add(oldReceiver);
                    }
                }
            }

            List<SysUser> sysUsers = batchSaveOrUpdate(result);
            if (CollectionUtils.isNotEmpty(sysUsers)) {
                dynamicShiroFilterFactoryBean.updateUserPermission();
            }

            return sysUsers;
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public SysUser setUserRole(String employeeId, String roleName, String appid) {
        SysUser sysUser = findByEmployeeId(employeeId);
        List<SysRole> sysRoles = sysRoleService.findByAppidAndRoleName(appid, roleName);

        if (CollectionUtils.isNotEmpty(sysRoles)) {
            Set<SysRole> sysRoleSet = new HashSet(sysRoles);
            sysUser.setRoles(sysRoleSet);
            return saveOrUpdate(sysUser);
        } else {
            return null;
        }
    }

    @CacheResult(cacheName = "SysPermission")
    public List<SysPermission> getMenu() {

        String userID = CurrentUser.getUserID();
        SysUser sysUser = findById(userID);

        List<SysPermission> sysPermissions = new ArrayList<>();

        if (null != sysUser) {
            sysPermissions = sysUser.getRoles().stream()
                    .flatMap(sysRole -> sysRole.getPermissions().stream())
                    .filter(sysPermission -> null == sysPermission.getParent())
                    .sorted(Comparator.comparing(SysPermission::getRanking))
                    .collect(toList());

        }

        return sysPermissions;
    }
}
