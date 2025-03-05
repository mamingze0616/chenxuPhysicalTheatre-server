package com.chenxu.physical.theatre.database.constant;

/**
 * @author mamingze
 * @version 1.0
 * @title TUserStatus
 * @description
 * @create 2025/3/4 17:25
 */
public enum TUserStatus {
    LOGOFF(0, "注销"),
    NORMAL(1, "正常"),
    LOCKED(2, "锁定"),
    DELETED(3, "删除");

    private final Integer code;
    private final String desc;

    TUserStatus(Integer code, String desc) {
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
