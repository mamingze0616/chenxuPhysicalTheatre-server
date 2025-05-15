package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TUserOrderStatus
 * @description
 * @create 2025/5/12 16:33
 */
public enum TUserOrderStatus {

    /**
     * 1:未支付;2:已支付;3:过期取消支付;4:作废
     */
    UNPAID(1, "未支付"),
    PAID(2, "已支付"),
    CANCELED(3, "过期取消支付"),
    DISABLED(4, "作废");


    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TUserOrderStatus(Integer code, String desc) {
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
