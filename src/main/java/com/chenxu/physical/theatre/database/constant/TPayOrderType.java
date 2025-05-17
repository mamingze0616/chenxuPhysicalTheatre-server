package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TPayOrderType
 * @description
 * @create 2025/5/12 16:46
 */
public enum TPayOrderType {
    /**
     * 1:会员;2:课程;3:形体服
     */
    MEMBERSHIP(1, "会员"),
    COURSE(2, "课程"),
    CLOTHES(3, "形体服"),
    ACTIVITY(4, "活动");
    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TPayOrderType(Integer code, String desc) {
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
