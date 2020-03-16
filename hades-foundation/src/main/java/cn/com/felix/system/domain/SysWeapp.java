package cn.com.felix.system.domain;

import cn.com.felix.common.basic.domain.BaseSysDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "sys_weapp", indexes = {
        @Index(name = "sys_weapp_wid_idx", columnList = "wid"),
        @Index(name = "sys_weapp_appid_idx", columnList = "appid")})
@Cacheable
@org.hibernate.annotations.Cache(region = "SysWeapp", usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysWeapp extends BaseSysDomain {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "wid", length = 64)
    private String wid;

    @Column(name = "appid", length = 64)
    private String appid;

    @Column(name = "weapp_name", length = 128)
    private String weappName;

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getWeappName() {
        return weappName;
    }

    public void setWeappName(String weappName) {
        this.weappName = weappName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("wid", wid)
                .append("appid", appid)
                .append("weappName", weappName)
                .toString();
    }
}
