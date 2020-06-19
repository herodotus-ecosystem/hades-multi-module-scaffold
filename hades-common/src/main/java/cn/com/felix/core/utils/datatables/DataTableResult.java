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
 * Module Name: hades-common
 * File Name: DataTableResult.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.utils.datatables;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataTableResult implements Serializable {

    private int pageNumber;
    private int pageSize;
    private String sEcho;
    private int iDisplayStart = 0;
    private int iDisplayLength = 0;
    private String jsonString;
    private long total;

    public DataTableResult(String sEcho, int iDisplayStart, int iDisplayLength, String jsonString) {
        this.sEcho = sEcho;
        this.iDisplayStart = iDisplayStart;
        this.iDisplayLength = iDisplayLength;
        this.pageNumber = this.iDisplayStart / this.iDisplayLength;
        this.pageSize = this.iDisplayLength;
        this.jsonString = jsonString;
    }

}
