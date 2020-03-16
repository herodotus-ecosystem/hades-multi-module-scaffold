/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: DepartmentRepository
 * Author: gengwei.zheng
 * Date: 19-2-14 下午3:59
 * LastModified: 19-2-14 下午3:59
 */

package cn.com.felix.hr.repository;

import cn.com.felix.hr.domain.Orgnization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
 * @date 2019/2/14
 */
public interface OrgnizationRepository extends JpaRepository<Orgnization, String> {
    Orgnization findByPkid(String pkid);

    @Modifying
    @Transactional
    @Query("delete from Orgnization e where e.pkid = ?1")
    void deleteByPkid(String pkid);
}
