package com.chenxu.physical.theatre.bussiness.dto;

import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import lombok.Data;

import java.util.List;

/**
 * @author mamingze
 * @version 1.0
 * @title ApiOverviewOfCourseNumberModel
 * @description
 * @create 2025/3/29 10:02
 */
@Data
public class ApiOverviewOfCourseNumberModel {
    //总课程数量
    private Integer totalCourseNumber;
    //已学过的课程数量(包含已签到)
    private Integer learnedNumber;
    //已预约的课程数量
    private Integer appointedNumber;
    private List<TAppointmentInfo> totalCourseList;
    private List<TAppointmentInfo> learnedCourseList;
    private List<TAppointmentInfo> appointedCourseList;


}
