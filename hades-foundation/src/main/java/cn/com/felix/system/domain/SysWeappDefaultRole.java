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
 * File Name: SysWeappDefaultRole.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.system.domain;

import cn.com.felix.common.basic.domain.BaseSysDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * <p>Description: 不同单位人员进入系统，其默认角色不同。
 * 根据不同的单位，指定不同的默认角色
 * </p>
 *
 * @author hades
 * @date 2019/3/11
 */

@Entity
@Table(name = "sys_weapp_default_role", indexes = {
        @Index(name = "sys_weapp_defaultrole_wdrid_idx", columnList = "wdrid"),
        @Index(name = "sys_weapp_defaultrole_appid_idx", columnList = "appid"),
        @Index(name = "sys_weapp_defaultrole_oid_idx", columnList = "orgnizationid"),
        @Index(name = "sys_weapp_defaultrole_uid_idx", columnList = "uid")})
@Cacheable
@org.hibernate.annotations.Cache(region = "SysWeappDefaultRole", usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysWeappDefaultRole extends BaseSysDomain {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "wdrid", length = 64)
    private String wdrid;

    @Column(name = "appid", length = 64)
    private String appid;

    @Column(name = "orgnizationid", length = 64)
    private String orgnizationid;

    /**
     * 预约人
     */
    @ManyToOne
    @JoinColumn(name = "uid")
    private SysRole role;

    public String getWdrid() {
        return wdrid;
    }

    public void setWdrid(String wdrid) {
        this.wdrid = wdrid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOrgnizationid() {
        return orgnizationid;
    }

    public void setOrgnizationid(String orgnizationid) {
        this.orgnizationid = orgnizationid;
    }

    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("wdrid", wdrid)
                .append("appid", appid)
                .append("orgnizationid", orgnizationid)
                .append("role", role)
                .toString();
    }
}
