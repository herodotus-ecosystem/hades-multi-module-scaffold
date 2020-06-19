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
 * File Name: CustomPostgreSQLDialect.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.extend.hibernate.postgresql;

import org.hibernate.dialect.PostgreSQL9Dialect;

import java.sql.Types;

public class CustomPostgreSQLDialect extends PostgreSQL9Dialect {

    public CustomPostgreSQLDialect() {
        super();
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
        this.registerColumnType(Types.ARRAY, "text[]");
    }

    /**
     * 重载getAddForeignKeyConstraintString方法，阻止ddl-auto添加外键关联
     * @param constraintName
     * @param foreignKey
     * @param referencedTable
     * @param primaryKey
     * @param referencesPrimaryKey
     * @return
     */
    @Override
    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
        //  设置foreignkey对应的列值可以为空
        return " alter " + foreignKey[0] + " set default null ";
    }
}
