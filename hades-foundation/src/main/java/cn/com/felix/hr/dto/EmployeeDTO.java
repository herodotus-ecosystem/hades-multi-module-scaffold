/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: EmployeeDTO
 * Author: gengwei.zheng
 * Date: 19-3-8 上午10:54
 * LastModified: 19-3-8 上午10:54
 */

package cn.com.felix.hr.dto;

import cn.com.felix.common.basic.dto.BaseDTO;
import cn.com.felix.core.enums.Identity;
import cn.com.felix.weapp.dto.component.WuxSelectOptionDTO;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
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
