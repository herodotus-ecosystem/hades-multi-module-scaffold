/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: Department
 * Author: hades
 * Date: 18-12-29 上午8:41
 * LastModified: 18-12-25 下午5:10
 */

package cn.com.felix.hr.domain;

import cn.com.felix.common.basic.domain.BaseAppDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "sys_department", indexes = {@Index(name = "sys_department_pkid_idx", columnList = "pkid")})
public class Department extends BaseAppDomain {

    @Column(name = "department_name", length = 1000)
    private String departmentName;

    @Column(name = "parentid", length = 64)
    private String parentId;

    @Column(name = "orgnizationid", length = 64)
    private String orgnizationId;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrgnizationId() {
        return orgnizationId;
    }

    public void setOrgnizationId(String orgnizationId) {
        this.orgnizationId = orgnizationId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("departmentName", departmentName)
                .append("parentId", parentId)
                .append("orgnizationId", orgnizationId)
                .toString();
    }
}
