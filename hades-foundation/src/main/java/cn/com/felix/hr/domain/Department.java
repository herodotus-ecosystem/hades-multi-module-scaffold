/*
 * Copyright (c) 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Project Name: hades-platform
 * Module Name: hades-foundation
 * File Name: Department.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
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
