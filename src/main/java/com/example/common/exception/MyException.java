package com.example.common.exception;

/**
 * @author: dj
 * @create: 2021-07-01 15:58
 * @description: 自定义错误
 **/
public class MyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;
    private final Long timestamp;

    public MyException(String msg) {
        super(msg);
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }

    public MyException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }

    public MyException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }

    public MyException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
