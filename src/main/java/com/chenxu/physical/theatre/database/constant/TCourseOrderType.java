package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author mamingze
 * @version 1.0
 * @title TCourseOrderType
 * @description
 * @create 2025/3/28 16:08
 */
@Getter
public enum TCourseOrderType {
    //1:赠送;2:支付
    GIVE_AWAY(1, "赠送"),
    PAY(2, "支付");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TCourseOrderType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }
}
