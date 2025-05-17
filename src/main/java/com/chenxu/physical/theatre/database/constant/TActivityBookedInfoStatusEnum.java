package com.chenxu.physical.theatre.database.constant;

/**
 * @author mamingze
 * @version 1.0
 * @title TActivityBookedInfoStatusEnum
 * @description
 * @create 2025/5/18 06:28
 */
public enum TActivityBookedInfoStatusEnum {
    //1:已支付;2:未支付;4:未签到;5:已签到
    PAYED(1, "已支付"),
    UNPAYED(2, "未支付"),
    UNSIGNED(4, "未签到"),
    SIGNED(5, "已签到");

    private final Integer code;
    private final String desc;

    TActivityBookedInfoStatusEnum(Integer code, String desc) {
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
        return code.toString();
    }

}
