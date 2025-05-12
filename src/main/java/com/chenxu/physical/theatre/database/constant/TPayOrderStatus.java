package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TPayOrderStatus
 * @description
 * @create 2025/5/12 18:55
 */
public enum TPayOrderStatus {
    /**
     * 1:未支付;2:已支付;3:已取消
     */
    UNPAID(1, "未支付"),
    PAID(2, "已支付"),
    CANCELED(3, "已取消");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TPayOrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }

}
