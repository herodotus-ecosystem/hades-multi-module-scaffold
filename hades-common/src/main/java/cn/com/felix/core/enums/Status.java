package cn.com.felix.core.enums;

public enum Status {
    FAILURE(0, "失败"),
    SUCCESS(1, "成功");

    private int code;
    private String descrption;

    Status(int code, String descrption) {
        this.code = code;
        this.descrption = descrption;
    }

    public int getCode() {
        return code;
    }

    public String getDescrption() {
        return descrption;
    }
}
