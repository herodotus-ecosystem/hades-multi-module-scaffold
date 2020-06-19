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
 * File Name: DataTableUtils.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.utils.datatables;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 *
 */
public class DataTableUtils {

    public static final String ECHO = "sEcho";
    public static final String DISPLAY_START = "iDisplayStart";
    public static final String DISPLAY_LENGTH = "iDisplayLength";
    public static final String QUERY_JSON = "queryJson";
    public static final String DATA = "data";

    public static DataTableResult parseDataTableParameter(String aoData) {

        List<DataTableParameter> params = JSON.parseArray(aoData, DataTableParameter.class);

        String sEcho = null;
        String jsonString = null;
        int iDisplayStart = 0;
        int iDisplayLength = 0;
        for (DataTableParameter param : params) {
            if (param.getName().equals(ECHO)) {
                sEcho = param.getValue().toString();
            }
            if (param.getName().equals(DISPLAY_START)) {
                iDisplayStart = (int) param.getValue();
            }
            if (param.getName().equals(DISPLAY_LENGTH)) {
                iDisplayLength = (int) param.getValue();
            }
            if (param.getName().equals(QUERY_JSON)) {
                jsonString = param.getValue().toString();
            }
        }

        return new DataTableResult(sEcho, iDisplayStart, iDisplayLength, jsonString);
    }
}
