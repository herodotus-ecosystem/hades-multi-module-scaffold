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
