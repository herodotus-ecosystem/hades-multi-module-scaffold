package cn.com.felix.core.extend.hibernate.postgresql;


import cn.com.felix.core.extend.hibernate.AbstractArrayType;

import java.math.BigDecimal;


public class NumericArrayType extends AbstractArrayType<BigDecimal> {

    @Override
    public Class returnedClass() {
        return BigDecimal[].class;
    }

    @Override
    protected Object safeReturnedObject() {
        return new BigDecimal[]{};
    }

    @Override
    protected String dbRealTypeName() {
        return "NUMERIC";
    }
}
