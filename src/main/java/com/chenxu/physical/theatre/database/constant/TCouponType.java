package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TCouponType
 * @description
 * @create 2025/5/7 00:14
 */
public enum TCouponType {
    //1:课程;2;折扣;3:委培制定优惠券;4:购买课程指定优惠券
    COUPON_TYPE_COURSE(1, "课程"),
    COUPON_TYPE_DISCOUNT(2, "折扣"),
    COUPON_TYPE_WORK_EMPLOYEE(3, "委培指定优惠券"),
    COUPON_TYPE_BUY_COURSE(4, "购买课程指定优惠券");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TCouponType(Integer code, String desc) {
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
