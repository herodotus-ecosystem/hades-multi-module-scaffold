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
 * File Name: DateUtils.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STANDARD_FORMAT);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public static String getCurrentTime() {
        return simpleDateFormat.format(new Date());
    }

    public static String getDateTime(Date date){
        if(date ==null) {
            return "";
        }
        return dateFormat.format(date);
    }


    public static String getFullDateTime(Date date){
        if(date ==null) {
            return "";
        }
        return simpleDateFormat.format(date);
    }

    /**
     * @title: dateCompare
     * @description: 比较日期大小
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static int dateCompare(Date date1, Date date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateFirst = dateFormat.format(date1);
        String dateLast = dateFormat.format(date2);
        int dateFirstIntVal = Integer.parseInt(dateFirst);
        int dateLastIntVal = Integer.parseInt(dateLast);
        if (dateFirstIntVal > dateLastIntVal) {
            return 1;
        } else if (dateFirstIntVal < dateLastIntVal) {
            return -1;
        }
        return 0;
    }
}
