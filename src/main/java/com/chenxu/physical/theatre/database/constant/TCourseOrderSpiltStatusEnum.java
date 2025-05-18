package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TCourseOrderSpiltStatusEnum
 * @description
 * @create 2025/5/19 00:09
 */
public enum TCourseOrderSpiltStatusEnum {
    //1:已核销;2:未核销;3:已过期
    WRITE_OFF(1, "已核销"),
    UNWRITE_OFF(2, "未核销"),
    EXPIRED(3, "已过期");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TCourseOrderSpiltStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }
}
