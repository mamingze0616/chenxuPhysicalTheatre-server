package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TCouponAlgorithmEnum
 * @description
 * @create 2025/5/17 04:21
 */
public enum TCouponAlgorithmEnum {
    //1:打折;2:满减
    DISCOUNT(1, "打折"),
    FULL_REDUCTION(2, "满减");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TCouponAlgorithmEnum(Integer code, String desc) {
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
