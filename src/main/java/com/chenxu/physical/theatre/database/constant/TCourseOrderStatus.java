package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author mamingze
 * @version 1.0
 * @title TCourseOrderStatus
 * @description
 * @create 2025/3/28 11:48
 */
@Getter
public enum TCourseOrderStatus {
    NORMAL(1, "正常"),
    DELETED(2, "作废"),
    UNPAID(3, "未支付"),
    SUCCESS(4, "已支付");
    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TCourseOrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }

}
