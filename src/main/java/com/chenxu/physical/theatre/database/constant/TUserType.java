package com.chenxu.physical.theatre.database.constant;

/**
 * @author mamingze
 * @version 1.0
 * @title TUserType
 * @description
 * @create 2025/3/4 17:22
 */
public enum TUserType {
    ADMIN(1, "管理员"),
    USER(2, "普通用户");

    private final Integer code;
    private final String desc;

    TUserType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
