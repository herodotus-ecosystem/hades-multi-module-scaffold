package cn.com.felix.system.domain;

import cn.com.felix.common.basic.domain.BaseSysDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: TODO </p>
 *
 * @author : hades
 * @date : 2019/11/12 16:30
 */
@Entity
@Table(name = "sys_role",
        indexes = {@Index(name = "sys_role_rid_idx", columnList = "rid")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"role_name"})})
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "SysRoleWithAndAuthority",
                attributeNodes = {
                        @NamedAttributeNode(value = "permissions")
                })
})
public class SysRole extends BaseSysDomain {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "rid", length = 64)
    private String rid;

    @Column(name = "role_name", length = 128, unique = true)
    private String roleName;

    @Column(name = "description", length = 2048)
    private String description;

    /**
     * 用户 - 角色关系定义:
     * (1) 加上fetch=FetchType.LAZY  或 @Fetch(FetchMode.SELECT), 输出结果与上面相同，说明默认设置是fetch=FetchType.LAZY 和 @Fetch(FetchMode.SELECT) 下面四种配置等效，都是N+1条sql的懒加载
     * (2) 加上fetch=FetchType.Eager 和 @Fetch(FetchMode.SELECT), 同样是N+1条sql，不过和上面情况不同的是，N条sql会在criteria.list()时执行
     * (3) 加上@Fetch(FetchMode.JOIN), 那么Hibernate将强行设置为fetch=FetchType.EAGER, 用户设置fetch=FetchType.LAZY将不会生效
     * 从输出可看出，在执行criteria.list()时通过一条sql 获取了所有的City和Hotel。
     * 使用@Fetch(FetchMode.JOIN)需要注意的是：它在Join查询时是Full Join, 所以会有重复City出现
     * (4) 加上@Fetch(FetchMode.SUBSELECT), 那么Hibernate将强行设置为fetch=FetchType.EAGER, 用户设置fetch=FetchType.LAZY将不会生效 从输出可看出，在执行criteria.list()时通过两条sql分别获取City和Hotel
     *
     * @link：https://www.jianshu.com/p/23bd82a7b96e
     */
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<SysUser> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "sys_role_permission",
            joinColumns = {@JoinColumn(name = "rid")},
            inverseJoinColumns = {@JoinColumn(name = "pid")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"rid", "pid"})},
            indexes = {@Index(name = "sys_role_menu_rid_idx", columnList = "rid"), @Index(name = "sys_role_menu_pid_idx", columnList = "pid")})
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Set<SysPermission> permissions = new HashSet<>();

    @Column(name = "appid", length = 64)
    private String appid;

    @Column(name = "role_type", length = 256)
    private String roleType;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<SysUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SysUser> users) {
        this.users = users;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SysRole sysRole = (SysRole) o;

        return new EqualsBuilder()
                .append(rid, sysRole.rid)
                .append(roleName, sysRole.roleName)
                .append(appid, sysRole.appid)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(rid)
                .append(roleName)
                .append(appid)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "rid='" + rid + '\'' +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                ", appid='" + appid + '\'' +
                ", roleType='" + roleType + '\'' +
                '}';
    }
}
