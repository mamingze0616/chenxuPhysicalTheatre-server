package com.chenxu.physical.theatre.database.constant;

/**
 * @author mamingze
 * @version 1.0
 * @title TCourseOrderStatus
 * @description
 * @create 2025/3/28 11:48
 */
public enum TCourseOrderStatus {
    NORMAL(1, "正常"),
    DELETED(2, "作废");
    private final Integer code;
    private final String desc;

    TCourseOrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

}
