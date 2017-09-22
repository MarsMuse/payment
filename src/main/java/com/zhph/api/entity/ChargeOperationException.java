package com.zhph.api.entity;

public class ChargeOperationException extends  RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code;

    private String message;

    public ChargeOperationException(String message, String code, String message1) {
        super(message);
        this.code = code;
        this.message = message1;
    }

    public ChargeOperationException(Throwable cause, String code, String message) {
        super(message,cause);
        this.code = code;
        this.message = message;
    }

    public ChargeOperationException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ChargeOperationException(String message) {
        super(message);
        this.message = message;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
