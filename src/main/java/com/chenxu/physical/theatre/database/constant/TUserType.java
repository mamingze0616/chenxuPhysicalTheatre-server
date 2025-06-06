package com.chenxu.physical.theatre.database.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author mamingze
 * @version 1.0
 * @title TUserType
 * @description
 * @create 2025/3/4 17:22
 */
@Getter
public enum TUserType {
    ADMIN(1, "管理员"),
    SUPER_ADMIN(2, "超级管理员"),
    TEACHER(3, "视频用户"),
    NON_MEMBER(4, "非会员"),
    MEMBER(5, "会员用户");
    @EnumValue
    @JsonValue
    private final Integer code;

    private final String desc;

    TUserType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }
}
