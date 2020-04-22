/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: SysWeappDefaultRoleRepository
 * Author: hades
 * Date: 19-3-11 下午3:10
 * LastModified: 19-3-11 下午3:10
 */

package cn.com.felix.system.repository;

import cn.com.felix.system.domain.SysWeappDefaultRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/3/11
 */
public interface SysWeappDefaultRoleRepository extends JpaRepository<SysWeappDefaultRole, String> {

    SysWeappDefaultRole findByAppidAndOrgnizationid(String appid, String orgnizationid);
}
