package cn.com.felix.core.extend.hibernate.postgresql;

import cn.com.felix.core.utils.JacksonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationException;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class JsonbTypeCsdn implements UserType {


    private final ObjectMapper mapper = JacksonUtils.getObjectMapper();


    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            try{
                st.setObject(index, mapper.writeValueAsString(value), Types.OTHER);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public Object deepCopy(Object originalValue) throws HibernateException {
        if (originalValue != null) {
            try {
                return mapper.readValue(mapper.writeValueAsString(originalValue),
                        returnedClass());
            } catch (IOException e) {
                throw new HibernateException("Failed to deep copy object", e);
            }
        }
        return null;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        PGobject o = (PGobject) rs.getObject(names[0]);
        if (o.getValue() != null) {
            try {
                return mapper.readValue(o.getValue(),Map.class);

            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return new HashMap<String, Object>();
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        Object copy = deepCopy(value);

        if (copy instanceof Serializable) {
            return (Serializable) copy;
        }

        throw new SerializationException(String.format("Cannot serialize '%s', %s is not Serializable.", value, value.getClass()), null);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        if (x == null) {
            return 0;
        }

        return x.hashCode();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.nullSafeEquals(x, y);
    }

    @Override
    public Class<?> returnedClass() {
        return Map.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

//    @Override
//    public int[] sqlTypes() {
//        return new int[0];
//    }
//
//    @Override
//    public Class returnedClass() {
//        return null;
//    }
//
//    @Override
//    public boolean equals(Object o, Object o1) throws HibernateException {
//        return false;
//    }
//
//    @Override
//    public int hashCode(Object o) throws HibernateException {
//        return 0;
//    }
//
//    @Override
//    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
//        PGobject o = (PGobject) rs.getObject(names[0]);
//        if (o.getValue() != null) {
//            try {
//                return mapper.readValue(o.getValue(), Map.class);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return new HashMap<String, Object>();
//
//    }
//
//    @Override
//    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
//        if (value == null) {
//            st.setNull(index, Types.OTHER);
//        } else {
//            try {
//                st.setObject(index, mapper.writeValueAsString(value), Types.OTHER);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }
//
//    @Override
//    public Object deepCopy(Object originalValue) throws HibernateException {
//        if (originalValue != null) {
//            try {
//                return mapper.readValue(mapper.writeValueAsString(originalValue),
//                        returnedClass());
//            } catch (IOException e) {
//                throw new HibernateException("Failed to deep copy object", e);
//            }
//        }
//        return null;
//
//    }
//
//    @Override
//    public boolean isMutable() {
//        return false;
//    }
//
//    @Override
//    public Serializable disassemble(Object value) throws HibernateException {
//        Object copy = deepCopy(value);
//
//        if (copy instanceof Serializable) {
//            return (Serializable) copy;
//        }
//
//        throw new SerializationException(String.format("Cannot serialize '%s', %s is not Serializable.", value, value.getClass()), null);
//    }
//
//    @Override
//    public Object assemble(Serializable cached, Object o) throws HibernateException {
//        return deepCopy(cached);
//
//    }
//
//    @Override
//    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
//        return null;
//    }
}
