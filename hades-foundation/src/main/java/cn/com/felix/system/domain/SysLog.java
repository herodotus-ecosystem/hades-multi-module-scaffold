package cn.com.felix.system.domain;

import cn.com.felix.common.basic.domain.BaseDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@Table(name = "sys_log", indexes = {@Index(name = "sys_log_pkid_idx", columnList = "pkid")})
public class SysLog extends BaseDomain {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "pkid", length = 64)
    private String pkid;

    @Column(name = "uid", length = 64)
    private String uid;

    @Column(name = "user_name", length = 128)
    private String userName;

    @Column(name = "login_ip", length = 25)
    private String loginIp;

    @Column(name = "login_time", length = 256)
    private String loginTime;

    @Column(name = "login_action", length = 1024)
    private String loginAction;

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginAction() {
        return loginAction;
    }

    public void setLoginAction(String loginAction) {
        this.loginAction = loginAction;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pkid", pkid)
                .append("uid", uid)
                .append("userName", userName)
                .append("loginIp", loginIp)
                .append("loginTime", loginTime)
                .append("loginAction", loginAction)
                .toString();
    }
}
