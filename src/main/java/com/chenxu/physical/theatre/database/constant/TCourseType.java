package com.chenxu.physical.theatre.database.constant;

/**
 * @author mamingze
 * @version 1.0
 * @title TCourseType
 * @description
 * @create 2025/3/20 14:16
 */
public enum TCourseType {
    NOT_START(1, "未上"),
    FINISHED(2, "已上"),
    DELETED(3, "删除");

    private final Integer code;
    private final String desc;

    TCourseType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
   }
}
