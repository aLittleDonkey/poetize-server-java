package com.ld.poetry.handle;

public class PoetryRuntimeException extends RuntimeException {

    private String msg;

    public PoetryRuntimeException() {
        super();
    }

    public PoetryRuntimeException(String msg) {
        super(msg);
        this.msg = msg;
    }


    public PoetryRuntimeException(Throwable cause) {
        super(cause);
        this.msg = cause.getMessage();
    }

    public PoetryRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }
}
