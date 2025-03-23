package com.chenxu.physical.theatre.database.constant;

import java.time.DateTimeException;


/**
 * @author mamingze
 * @version 1.0
 * @title ChineseDayOfWeek
 * @description
 * @create 2025/3/23 16:53
 */
public enum ChineseDayOfWeek {
    MONDAY(1, "星期一"),
    TUESDAY(2, "星期二"),
    WEDNESDAY(3, "星期三"),
    THURSDAY(4, "星期四"),
    FRIDAY(5, "星期五"),
    SATURDAY(6, "星期六"),
    SUNDAY(7, "星期日");
    private static final ChineseDayOfWeek[] ENUMS = ChineseDayOfWeek.values();

    private final Integer code;
    private final String desc;

    ChineseDayOfWeek(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static ChineseDayOfWeek of(int dayOfWeek) {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new DateTimeException("Invalid value for DayOfWeek: " + dayOfWeek);
        }
        return ENUMS[dayOfWeek - 1];
    }
}
