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
 * File Name: AbstractArrayType.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.extend.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;
import java.sql.*;

/**
 * Created by hades on 2017/6/7.
 */
public abstract class AbstractArrayType<T extends Serializable> extends AbstractUserType {

    protected static final int[] SQL_TYPES = { Types.ARRAY };

    @Override
    public final int[] sqlTypes() {
        return SQL_TYPES;
    }

    protected abstract Object safeReturnedObject();

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        Array array = resultSet.getArray(strings[0]);
        T[] typeArray = (T[]) array.getArray();
        if (typeArray == null) {
            return safeReturnedObject();
        }

        return typeArray;
    }

    protected abstract String dbRealTypeName();

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        Connection connection = preparedStatement.getConnection();
        T[] typeArray = (T[]) value;
        Array array = connection.createArrayOf(dbRealTypeName(), typeArray);
        if (null != array) {
            preparedStatement.setArray(index, array);
        } else {
            preparedStatement.setNull(index, SQL_TYPES[0]);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value == null ? null : ((T[]) value).clone();
    }

}
