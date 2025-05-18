package com.chenxu.physical.theatre.bussiness.service;

import com.chenxu.physical.theatre.database.constant.TCourseOrderSpiltStatusEnum;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TCourseOrderSpilt;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import com.chenxu.physical.theatre.database.service.TCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author mamingze
 * @version 1.0
 * @title BookedCourseService
 * @description
 * @create 2025/5/19 01:44
 */
@Service
public class BookedCourseService {
    private static final Logger logger = LoggerFactory.getLogger(BookedCourseService.class);
    @Autowired
    TCourseService courseService;
    @Autowired
    TAppointmentInfoService appointmentInfoService;
    @Autowired
    CourseOrderSplitService courseOrderSplitService;

    public boolean doBookedCourse(TAppointmentInfo appointmentInfo) {
        try {
            Optional.ofNullable(appointmentInfo.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));
            if (appointmentInfoService.save(appointmentInfo)) {
                TCourseOrderSpilt courseOrderSpilt = courseOrderSplitService.getUnWriteOffCourseOrderSpilt(appointmentInfo.getUserId());
                courseOrderSpilt.setStatus(TCourseOrderSpiltStatusEnum.EXPIRED);
                courseOrderSpilt.setAppointmentId(appointmentInfo.getId());
                courseOrderSplitService.updateById(courseOrderSpilt);
                return courseService.updateCourseBookedNumber(appointmentInfo.getCourseId());
            } else {
                throw new RuntimeException("预约信息写入表失败");
            }
        } catch (Exception e) {
            throw e;
        }

    }
}
