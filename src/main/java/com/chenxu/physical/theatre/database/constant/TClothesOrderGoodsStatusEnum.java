package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TClothesOrderGoodsStatusEnum
 * @description
 * @create 2025/5/16 03:20
 */
public enum TClothesOrderGoodsStatusEnum {
    //1:已下发;2:未下发;3已发货;
    ISSUED(1, "已下发"),
    NOT_ISSUED(2, "未下发"),
    SHIPPED(3, "已发货");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TClothesOrderGoodsStatusEnum(Integer code, String desc) {
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
