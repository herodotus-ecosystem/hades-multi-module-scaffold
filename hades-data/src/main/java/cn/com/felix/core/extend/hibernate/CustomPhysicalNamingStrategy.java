package cn.com.felix.core.extend.hibernate;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Created by gengwei.zheng on 2017/6/1.
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
