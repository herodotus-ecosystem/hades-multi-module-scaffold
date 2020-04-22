/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: DepartmentDTO
 * Author: hades
 * Date: 19-3-8 下午1:02
 * LastModified: 19-3-8 下午1:02
 */

package cn.com.felix.hr.dto;

import cn.com.felix.common.basic.dto.BaseDTO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/3/8
 */
public class DepartmentDTO extends BaseDTO {

    private String departmentid;
    private String departmentName;
    private String orgnizationid;
    private Integer ranking;

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getOrgnizationid() {
        return orgnizationid;
    }

    public void setOrgnizationid(String orgnizationid) {
        this.orgnizationid = orgnizationid;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("departmentid", departmentid)
                .append("departmentName", departmentName)
                .append("orgnizationid", orgnizationid)
                .append("ranking", ranking)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DepartmentDTO that = (DepartmentDTO) o;

        return new EqualsBuilder()
                .append(departmentid, that.departmentid)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(departmentid)
                .toHashCode();
    }
}

