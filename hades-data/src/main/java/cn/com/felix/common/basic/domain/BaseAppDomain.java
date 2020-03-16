package cn.com.felix.common.basic.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * <p>Description: 一般应用功能基础实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/3 16:24
 */
@MappedSuperclass
public class BaseAppDomain extends BaseDomain{

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "pkid", length = 64)
    private String pkid;

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }
}
