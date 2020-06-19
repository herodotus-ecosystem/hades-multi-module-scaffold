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
 * File Name: Orgnization.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
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
