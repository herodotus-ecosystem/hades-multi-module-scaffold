package cn.com.felix.system.domain;

import cn.com.felix.common.basic.domain.BaseSysDomain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Description: TODO </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/12 16:31
 */
@Entity
@Table(name = "sys_permission", indexes = {@Index(name = "sys_permission_pid_idx", columnList = "pid")})
public class SysPermission extends BaseSysDomain {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "pid", length = 64)
    @JsonProperty("pid")
    private String pid;

    @Column(name = "permission_name", length = 1024)
    @JsonProperty("permissionName")
    private String permissionName;

    /**
     * 资源类型，[menu|button]
     */
    @Column(name = "resource_type", length = 50)
    @JsonProperty("resourceType")
    private String resourceType;

    @Column(name = "url", length = 4096)
    @JsonProperty("url")
    private String url;

    /**
     * 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
     */
    @Column(name = "permission", length = 2048)
    @JsonProperty("permission")
    private String permission;


    /**
     * 以下是权限的树形查询，经过几次尝试，以下是最优方案。
     * 如果 @ManyToOne 和 @OneToMany都设置成 FetchType.EAGER 会出现父节点找不到的问题。
     * 如果 @ManyToOne 和 @OneToMany都设置成 FetchType.LAZY 会出现Lazy失败问题。出现“could not initialize proxy - no Session”错误
     *
     * @link https://blog.csdn.net/blueheart20/article/details/52912023
     * <p>
     * 注解@JsonBackReference和@JsonManagedReference
     * 这两个标注通常配对使用，通常用在父子关系中。@JsonBackReference标注的属性在序列化（serialization，即将对象转换为json数据）时，会被忽略（即结果中的json数据不包含该属性的内容）
     * 。@JsonManagedReference标注的属性则会被序列化。在序列化时，@JsonBackReference的作用相当于@JsonIgnore，此时可以没有@JsonManagedReference。
     * 但在反序列化（deserialization，即json数据转换为对象）时，如果没有@JsonManagedReference，则不会自动注入@JsonBackReference标注的属性（被忽略的父或子）；
     * 如果有@JsonManagedReference，则会自动注入自动注入@JsonBackReference标注的属性。
     * <p>
     * 注解@JsonIgnore：直接忽略某个属性，以断开无限递归，序列化或反序列化均忽略。当然如果标注在get、set方法中，则可以分开控制，序列化对应的是get方法，反序列化对应的是set方法。
     * 在父子关系中，当反序列化时，@JsonIgnore不会自动注入被忽略的属性值（父或子），这是它跟@JsonBackReference和@JsonManagedReference最大的区别。
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonProperty("parent")
    @JsonBackReference
    private SysPermission parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonManagedReference
    private List<SysPermission> children = new ArrayList<>();

    @ManyToMany(mappedBy = "permissions")
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private Set<SysRole> roles = new HashSet<>();

    @Column(name = "menu_class", length = 50)
    private String menuClass = "fa fa-caret-right";

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Set<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRole> roles) {
        this.roles = roles;
    }

    public String getMenuClass() {
        return menuClass;
    }

    public void setMenuClass(String menuClass) {
        this.menuClass = menuClass;
    }

    public SysPermission getParent() {
        return parent;
    }

    public void setParent(SysPermission parent) {
        this.parent = parent;
    }

    public List<SysPermission> getChildren() {
        return children;
    }

    public void setChildren(List<SysPermission> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SysPermission that = (SysPermission) o;

        return new EqualsBuilder()
                .append(pid, that.pid)
                .append(permissionName, that.permissionName)
                .append(resourceType, that.resourceType)
                .append(url, that.url)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(pid)
                .append(permissionName)
                .append(resourceType)
                .append(url)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "SysPermission{" +
                "pid='" + pid + '\'' +
                ", permissionName='" + permissionName + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", url='" + url + '\'' +
                ", permission='" + permission + '\'' +
                ", menuClass='" + menuClass + '\'' +
                '}';
    }
}
