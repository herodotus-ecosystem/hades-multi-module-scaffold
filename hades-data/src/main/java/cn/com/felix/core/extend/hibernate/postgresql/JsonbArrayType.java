package cn.com.felix.core.extend.hibernate.postgresql;

/**
 * Created by hades on 2017/6/6.
 */
public class JsonbArrayType extends TextArrayType {

    @Override
    protected String dbRealTypeName() {
        return "JSONB";
    }
}
