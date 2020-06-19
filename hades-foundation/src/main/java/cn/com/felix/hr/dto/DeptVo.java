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
 * File Name: DeptVo.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.hr.dto;

import cn.com.felix.common.basic.domain.BaseAppDomain;
import lombok.Data;

import java.util.Date;

/**
 *
 */
@Data
public class DeptVo extends BaseAppDomain {

    private String departmentName;

    private String orgnizationId;

    private String orgnizationName;


    private String parentId;

    private String parentDepartmentName;


    public DeptVo(
            String pkid,
            Date createTime,
            String departmentName,
            String orgnizationId,
            String parentId,
            int ranking,
            Date updateTime,
            String orgnaizationName, String parentDepartmentName) {

        this.setPkid(pkid);
        this.setCreateTime(createTime);
        this.departmentName = departmentName;
        this.orgnizationId = orgnizationId;
        this.parentId = parentId;

        this.setRanking(ranking);
        this.setUpdateTime(updateTime);
        this.orgnizationName = orgnaizationName;
        this.parentDepartmentName = parentDepartmentName;
    }
}
