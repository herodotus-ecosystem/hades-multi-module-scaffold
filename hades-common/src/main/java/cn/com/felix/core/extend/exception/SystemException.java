package cn.com.felix.core.extend.exception;

import cn.com.felix.core.enums.ResultEnum;

public class SystemException extends RuntimeException {
    private String message;
    private int code = ResultEnum.UNKONWN_ERROR.getStatus();

    public SystemException(String message) {
        super(message);
        this.message = message;
    }

    public SystemException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public SystemException(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public SystemException(String message, int code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }

    public SystemException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.message = resultEnum.getMessage();
        this.code = resultEnum.getStatus();
    }

    public SystemException(ResultEnum resultEnum, Throwable e) {
        super(resultEnum.getMessage(), e);
        this.message = resultEnum.getMessage();
        this.code = resultEnum.getStatus();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
