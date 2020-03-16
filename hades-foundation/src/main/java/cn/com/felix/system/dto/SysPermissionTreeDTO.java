package cn.com.felix.system.dto;

import cn.com.felix.common.basic.domain.BaseDomain;

public class SysPermissionTreeDTO extends BaseDomain {

    private String id;

    private String pId;

    private String name;

    private Boolean checked = false;

    private Boolean open = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }
}
