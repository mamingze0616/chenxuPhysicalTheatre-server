package com.chenxu.physical.theatre.bussiness.dto;

import com.chenxu.physical.theatre.database.domain.TCourse;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * @author mamingze
 * @version 1.0
 * @title ApiWeekCourseModel
 * @description
 * @create 2025/3/23 16:43
 */
@Data
@ToString
public class ApiWeekCourseModel {
    private String weekday;
    private LocalDate date;
    private List<TCourse> list;
}
