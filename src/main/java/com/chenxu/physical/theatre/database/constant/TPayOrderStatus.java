package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TPayOrderStatuss
 * @description
 * @create 2025/5/12 18:55
 */
public enum TPayOrderStatus {
    /**
     * 1:未支付;2:已支付;3:已取消;4:正在支付;5:已关闭;6:已撤销;7:支付失败;8:转入退款
     */
    UNPAID(1, "未支付"),
    SUCCESS(2, "已支付"),
    CANCELED(3, "已取消"),
    PAYING(4, "正在支付"),
    CLOSED(5, "已关闭"),
    REVOKED(6, "已撤销"),
    PAYERROR(7, "支付失败"),
    REFUND(8, "REFUND");

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
