package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TCourseType
 * @description
 * @create 2025/3/20 14:16
 */
public enum TCourseType {
    NOT_START(1, "未上"),
    FINISHED(2, "已上"),
    DELETED(3, "删除"),
    NOT_REGISTER(4, "未注册"),
    START_SIGNING_IN(5, "开始签到"),
    CANCELED(6, "课程取消");


    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TCourseType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code.toString();
    }

}
