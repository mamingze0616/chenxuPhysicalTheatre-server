package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author mamingze
 * @version 1.0
 * @title TAppointmentInfoTypeEnum
 * @description
 * @create 2025/3/29 10:11
 */
@Getter
public enum TAppointmentInfoTypeEnum {
    /**
     * 1:已预约;2:取消预约;3:已学;4:已签到
     */
    APPOINTED(1, "已预约"),
    CANCELED(2, "取消预约"),
    LEARNED(3, "已学"),
    SIGNED(4, "已签到");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TAppointmentInfoTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
