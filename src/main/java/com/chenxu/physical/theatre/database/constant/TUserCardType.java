package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mamingze
 * @version 1.0
 * @title TUserCardType
 * @description 买的课程卡类型
 * @create 2025/6/8 17:39
 */
public enum TUserCardType {
    //1:月卡;2:季卡;3:年卡;4:初级委培卡;5:中级委培卡;6:高级委培卡
    MONTH_CARD(1, "月卡"),
    QUARTER_CARD(2, "季卡"),
    YEAR_CARD(3, "年卡"),
    PRIMARY_EMPLOYEE_CARD(4, "初级委培卡"),
    MIDDLE_EMPLOYEE_CARD(5, "中级委培卡"),
    HIGH_EMPLOYEE_CARD(6, "高级委培卡");
    @EnumValue
    @JsonValue
    private final Integer code;

    private final String desc;

    TUserCardType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }

}
