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
 * File Name: SysWeapp.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.system.domain;

import cn.com.felix.common.basic.domain.BaseSysDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "sys_weapp", indexes = {
        @Index(name = "sys_weapp_wid_idx", columnList = "wid"),
        @Index(name = "sys_weapp_appid_idx", columnList = "appid")})
@Cacheable
@org.hibernate.annotations.Cache(region = "SysWeapp", usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysWeapp extends BaseSysDomain {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "wid", length = 64)
    private String wid;

    @Column(name = "appid", length = 64)
    private String appid;

    @Column(name = "weapp_name", length = 128)
    private String weappName;

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getWeappName() {
        return weappName;
    }

    public void setWeappName(String weappName) {
        this.weappName = weappName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("wid", wid)
                .append("appid", appid)
                .append("weappName", weappName)
                .toString();
    }
}
