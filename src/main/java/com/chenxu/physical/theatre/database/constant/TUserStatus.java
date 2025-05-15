package com.chenxu.physical.theatre.database.constant;

import lombok.Getter;

/**
 * @author mamingze
 * @version 1.0
 * @title TUserStatus
 * @description
 * @create 2025/3/4 17:25
 */
@Getter
public enum TUserStatus {
    LOGOFF(0, "注销"),
    NORMAL(1, "正常"),
    LOCKED(2, "锁定"),
    DELETED(3, "删除"),
    NEW_ADDED(4, "新注册");

    private final Integer code;
    private final String desc;

    TUserStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }
}
