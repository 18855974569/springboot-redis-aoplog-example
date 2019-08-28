package com.wjx.config.common;

import com.wjx.config.exception.BizErrorCodeEnum;

import java.io.Serializable;

/**
 * 微服务rest api  返回的result
 */
public class Result implements Serializable {

    private String code;
    private String msg;
    private Object data;

    public Result() {
    }

    public Result(BizErrorCodeEnum error) {
        this.code = error.getCode();
        this.msg = error.getMsg();
        this.data = null;
    }

    public Result(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
