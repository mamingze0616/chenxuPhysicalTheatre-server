package com.chenxu.physical.theatre.bussiness.dto;

/**
 * @author mamingze
 * @version 1.0
 * @title ApiResponse
 * @description 微信后端统一返回接口 code=1 ，返回成功，code=0 失败。
 * @create 2023/8/6 18:40
 */
public class ApiResponse {
    private Integer code;
    private String errorMsg;
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
