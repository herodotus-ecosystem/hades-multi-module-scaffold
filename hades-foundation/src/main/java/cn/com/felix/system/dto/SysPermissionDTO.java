package cn.com.felix.system.dto;

import cn.com.felix.common.basic.dto.BaseDTO;

public class SysPermissionDTO extends BaseDTO {

    private String pid;
    private String permissionName;
    private String resourceType = "menu";
    private String url;
    private String permission;
    private String parentId;
    private Boolean available = Boolean.TRUE;
    private String menuClass = "widgets";

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getMenuClass() {
        return menuClass;
    }

    public void setMenuClass(String menuClass) {
        this.menuClass = menuClass;
    }

    @Override
    public String toString() {
        return "SysPermissionDTO [pid=" + pid + ", permissionName=" + permissionName + ", resourceType=" + resourceType + ", url=" + url + ", permission=" + permission + ", available=" + available + ", menuClass=" + menuClass + ", parentId=" + parentId + "]";
    }
}
