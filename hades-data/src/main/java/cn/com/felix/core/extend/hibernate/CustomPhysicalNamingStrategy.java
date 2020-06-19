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
 * File Name: CustomPhysicalNamingStrategy.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.extend.hibernate;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Created by hades on 2017/6/1.
 *
 * 使用hbm2ddl自动创建表时，默认将@Colume中的信息转换为小写，小写的字段名称与南网集控的字段标准不同（驼峰式，单词首字母大写）
 * 复写原始类，生成符合标准的字段名称。
 */
public class CustomPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {

        // Hibernate 默认使用 Identifier.getCanonicalName()的值最为最终的值，text是原始值。如果quoted为true则使用text，否则就进行小写转换。所以此处quoted设置为true。参见具体方法。
        return new Identifier(name.getText(), true);
    }
}
