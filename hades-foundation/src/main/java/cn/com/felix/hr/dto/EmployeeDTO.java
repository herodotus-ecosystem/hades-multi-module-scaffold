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
 * File Name: EmployeeDTO.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.hr.dto;

import cn.com.felix.common.basic.dto.BaseDTO;
import cn.com.felix.core.enums.Identity;
import cn.com.felix.weapp.dto.component.WuxSelectOptionDTO;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/3/8
 */
public class EmployeeDTO extends WuxSelectOptionDTO {

    private String departmentid;
    private String orgnizationid;
    private String phoneNumber;
    private Identity identity;

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public String getOrgnizationid() {
        return orgnizationid;
    }

    public void setOrgnizationid(String orgnizationid) {
        this.orgnizationid = orgnizationid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("departmentid", departmentid)
                .append("orgnizationid", orgnizationid)
                .append("phoneNumber", phoneNumber)
                .append("identity", identity)
                .toString();
    }
}
