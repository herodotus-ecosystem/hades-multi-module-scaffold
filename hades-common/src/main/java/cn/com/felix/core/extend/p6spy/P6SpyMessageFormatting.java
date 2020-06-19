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
 * File Name: P6SpyMessageFormatting.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.extend.p6spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/1/20
 */
public class P6SpyMessageFormatting implements MessageFormattingStrategy {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");



    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {

        StringBuilder builder = new StringBuilder(this.format.format(new Date()));
        builder.append(" | took ");
        builder.append(elapsed);
        builder.append("ms | ");
        builder.append(category);
        builder.append(" | connection ");
        builder.append(connectionId);
        builder.append(" | url ");
        builder.append(url);
        builder.append("\n------------------------| ");
        builder.append(sql);
        builder.append(";");

        return StringUtils.isNotEmpty(sql.trim()) ? String.valueOf(builder) : "";
    }
}
