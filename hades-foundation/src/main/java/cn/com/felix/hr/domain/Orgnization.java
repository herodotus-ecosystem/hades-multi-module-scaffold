/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: Orgnization
 * Author: gengwei.zheng
 * Date: 18-12-29 上午8:41
 * LastModified: 18-12-25 下午5:10
 */

package cn.com.felix.hr.domain;

import cn.com.felix.common.basic.domain.BaseAppDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@Table(name = "sys_orgnization",
        indexes = {@Index(name = "sys_orgnization_pkid_idx", columnList = "pkid")},
        uniqueConstraints = {@UniqueConstraint(columnNames = "orgnaization_name")})
public class Orgnization extends BaseAppDomain {

    @Column(name = "orgnaization_name", length = 1000, unique = true)
    private String orgnaizationName;

    @Column(name = "parentid", length = 64)
    private String parentId;

    public String getOrgnaizationName() {
        return orgnaizationName;
    }

    public void setOrgnaizationName(String orgnaizationName) {
        this.orgnaizationName = orgnaizationName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("orgnaizationName", orgnaizationName)
                .append("parentId", parentId)
                .toString();
    }
}
