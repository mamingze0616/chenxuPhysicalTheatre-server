package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TUserCouponsStatus
 * @description
 * @create 2025/5/7 00:02
 */
public enum TUserCouponsStatus {
    NOT_USE(1, "未使用"),
    FINISHED(2, "已使用"),
    EXPIRE(3, "已过期");
    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TUserCouponsStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }
}
