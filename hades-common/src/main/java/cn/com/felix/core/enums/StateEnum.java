package cn.com.felix.core.enums;

public enum StateEnum {

    DEFAULT(0, "默认角色，不能刪除"),
    NORMAL(1, "正常"),
    MANAGE(2, "管理角色");

    private Integer status;
    private String description;

    StateEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getDescription() {
        return this.description;
    }
}
