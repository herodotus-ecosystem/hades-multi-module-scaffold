package cn.com.felix.core.extend.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;
import java.sql.*;

/**
 * Created by gengwei.zheng on 2017/6/7.
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
