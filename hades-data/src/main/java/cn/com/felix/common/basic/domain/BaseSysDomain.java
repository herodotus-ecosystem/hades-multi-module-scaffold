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
 * Module Name: hades-data
 * File Name: BaseSysDomain.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.common.basic.domain;

import cn.com.felix.core.enums.StateEnum;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

/**
 * <p>Description: 脚手架核心功能基础实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/3 16:25
 */
@MappedSuperclass
public abstract class BaseSysDomain extends BaseDomain {

    @Column(name = "state")
    private StateEnum state = StateEnum.NORMAL;

    @Column(name = "available")
    private Boolean available = Boolean.TRUE;

    @Column(name = "reversion")
    @OrderBy("reversion asc")
    private Integer reversion = 0;

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getReversion() {
        return reversion;
    }

    public void setReversion(Integer reversion) {
        this.reversion = reversion;
    }
}
