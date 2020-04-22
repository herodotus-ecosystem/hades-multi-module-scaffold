package cn.com.felix.system.domain;

import cn.com.felix.common.basic.domain.BaseSysDomain;
import cn.com.felix.core.extend.AbstractUser;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: TODO </p>
 *
 * @author : hades
 * @date : 2019/11/12 16:29
 */
@Entity
@Table(name = "sys_user", indexes = {@Index(name = "sys_user_uid_idx", columnList = "uid")})
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "SysUserWithRolesAndAuthority",
                attributeNodes = {
                        @NamedAttributeNode(value = "roles", subgraph = "SysRoleWithAuthority")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "SysRoleWithAuthority",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "permissions")
                                })
                })
})

public class SysUser extends BaseSysDomain implements AbstractUser {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "uid", length = 64)
    private String uid;

    @Column(name = "user_name", length = 128)
    private String userName;

    @Column(name = "password", length = 64)
    private String password;

    @Column(name = "salt", length = 64)
    private String salt;


    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "uid")},
            inverseJoinColumns = {@JoinColumn(name = "rid")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"uid", "rid"})},
            indexes = {@Index(name = "sys_user_role_uid_idx", columnList = "uid"), @Index(name = "sys_user_role_rid_idx", columnList = "rid")})
    private Set<SysRole> roles = new HashSet<>();

    @Column(name = "employeeid", length = 64)
    private String employeeId;

    @Column(name = "openid", length = 64)
    private String openId;

    @Override
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Set<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRole> roles) {
        this.roles = roles;
    }

    /**
     * 密码盐.
     */
    public String getCredentialsSalt() {
        return this.userName + this.salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SysUser sysUser = (SysUser) o;

        return new EqualsBuilder()
                .append(uid, sysUser.uid)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(uid)
                .toHashCode();
    }

    /**
     * 此处做了特殊处理，authorizationCache缓存会存对象的toString的值。导致Redis里面对同一个User反复生成不同的Key存过多的数据。
     *
     * @return
     */
    @Override
    public String toString() {
        return uid;
    }
}
