/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: EmployeeService
 * Author: hades
 * Date: 18-12-29 上午8:50
 * LastModified: 18-12-25 下午5:13
 */

package cn.com.felix.hr.service;

import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.core.enums.Identity;
import cn.com.felix.core.utils.JacksonUtils;
import cn.com.felix.core.utils.datatables.DataTableResult;
import cn.com.felix.hr.domain.Employee;
import cn.com.felix.hr.dto.EmployeeEssentialDTO;
import cn.com.felix.hr.repository.EmployeeRepository;
import cn.com.felix.system.domain.SysUser;
import cn.com.felix.system.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Service
public class EmployeeService extends BaseService<Employee, String> {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    private final EmployeeRepository employeeRepository;
    private final SysUserService sysUserService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, SysUserService sysUserService) {
        this.employeeRepository = employeeRepository;
        this.sysUserService = sysUserService;
    }

    public Employee findByPhoneNumber(String phoneNumber) {
        return employeeRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Employee findById(String id) {
        return employeeRepository.findByPkid(id);
    }

    @Override
    public void deleteById(String id) {
        employeeRepository.deleteByPkid(id);
    }

    @Override
    public Page<Employee> findByPage(int pageNumber, int pageSize) {
        return employeeRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "pkid"));
    }


    @Transactional
    public EmployeeEssentialDTO essentialInformation(String openId) {

        SysUser sysUser = sysUserService.findByOpenId(openId);

        if (null != openId && null != sysUser) {
            Employee employee = findById(sysUser.getEmployeeId());

            EmployeeEssentialDTO employeeEssentialDTO = new EmployeeEssentialDTO();

            employeeEssentialDTO.setSysRoles(sysUser);
            employeeEssentialDTO.setEmployee(employee);

            return employeeEssentialDTO;
        } else {
            logger.error("[Service] |- Get Employee Essential Information Error for Reason : openid is [{}]", openId);
            return null;
        }
    }

    public List<Employee> findAllByOrgnizationIdAndIdentity(String orgnizationId, Identity identity) {
        return employeeRepository.findAllByOrgnizationIdAndIdentityOrderByRankingAsc(orgnizationId, identity);
    }

    public List<Employee> queryData(DataTableResult dataTableResult) {
        Employee terminal = JacksonUtils.toObject(dataTableResult.getJsonString(), Employee.class);

        String sql = "SELECT * FROM sys_employee t WHERE 1=1 ";
        String sqlCount = "SELECT count(*) FROM sys_employee t WHERE 1=1 ";

        String vSn = terminal.getDepartment() == null ? "" : terminal.getDepartment().getPkid();
        if (StringUtils.isNotEmpty(vSn)) {
            sql += "AND t.departmentid='" + vSn + "' ";
            sqlCount += "AND t.departmentid='" + vSn + "' ";
        }
        String vin = StringUtils.isEmpty(terminal.getOrgnizationId()) ? "" : terminal.getOrgnizationId();
        if (StringUtils.isNotEmpty(vin)) {
            sql += "AND t.orgnizationid='" + vin + "' ";
            sqlCount += "AND t.orgnizationid='" + vin + "' ";
        }

        sql+=" order by t.ranking asc  ";
        sql += "LIMIT" + " " + dataTableResult.getPageSize() + " OFFSET " + dataTableResult.getIDisplayStart();
        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query nativeQuery = em.createNativeQuery(sql, Employee.class);

        @SuppressWarnings({"unused", "unchecked"})
        List<Employee> termins = nativeQuery.getResultList();

        Query query = em.createNativeQuery(sqlCount);

        dataTableResult.setTotal(((BigInteger)query.getSingleResult()).longValue());

        if (em != null) {
            em.close();
        }
        return termins;
    }

    public Employee saveOrUpdate(Employee employee) {
        return employeeRepository.save(employee);
    }
}
