package com.chenxu.physical.theatre.database.constant;

import java.time.LocalTime;

/**
 * @author mamingze
 * @version 1.0
 * @title TcourseStartTime
 * @description
 * @create 2025/3/27 10:28
 */
public enum TcourseStartTime {
    LESSON_1(1, LocalTime.of(9, 30, 0)),
    LESSON_2(2, LocalTime.of(10, 45, 0)),
    LESSON_3(3, LocalTime.of(13, 0, 0)),
    LESSON_4(4, LocalTime.of(14, 15, 0)),
    LESSON_5(5, LocalTime.of(15, 30, 0)),
    LESSON_6(6, LocalTime.of(16, 45, 0)),
    LESSON_7(7, LocalTime.of(19, 00, 0)),
    LESSON_8(8, LocalTime.of(20, 15, 0));


    private final Integer code;
    private final LocalTime startTime;

    TcourseStartTime(Integer code, LocalTime startTime) {
        this.code = code;
        this.startTime = startTime;
    }

    public Integer getCode() {
        return code;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public static LocalTime getStartTimeByCode(Integer code) {
        for (TcourseStartTime tcourseStartTime : TcourseStartTime.values()) {
            if (tcourseStartTime.getCode().equals(code)) {
                return tcourseStartTime.getStartTime();
            }
        }
        return LESSON_1.getStartTime();
    }


}
