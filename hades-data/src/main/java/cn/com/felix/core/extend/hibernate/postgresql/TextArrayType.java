package cn.com.felix.core.extend.hibernate.postgresql;

import cn.com.felix.core.extend.hibernate.AbstractArrayType;

/**
 * Created by gengwei.zheng on 2017/6/6.
 */
public class TextArrayType extends AbstractArrayType<String> {

    @Override
    public Class returnedClass() {
        return String[].class;
    }

    @Override
    protected Object safeReturnedObject() {
        return new String[] {};
    }

    @Override
    protected String dbRealTypeName() {
        return "TEXT";
    }


}
