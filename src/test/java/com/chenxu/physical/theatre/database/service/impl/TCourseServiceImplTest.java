package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.controller.CourseController;
import com.chenxu.physical.theatre.bussiness.dto.ApiWeekCourseModel;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.service.TCourseService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


/**
 * @author mamingze
 * @version 1.0
 * @title TCourseServiceImplTest
 * @description
 * @create 2025/3/23 14:12
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class TCourseServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(TCourseServiceImplTest.class);
    @Autowired
    private CourseController courseController;
    @Autowired
    private TCourseService tCourseService;

    @Test
    void testGetTwoWeekCourses() {
        ApiWeekCourseModel apiDateRequest = new ApiWeekCourseModel();
        logger.info("testGetTwoWeekCourses::{}", courseController.getTwoWeekCourses("dddd", apiDateRequest));
    }


    @Test
    void getBookableCoursesWithAppointmentInfoByUserid() {
        logger.info(tCourseService.getBookableCoursesWithAppointmentInfoByUserid(Integer.valueOf(2)).toString());

    }

    @Test
    @Disabled
    void updateCourseType() {
        tCourseService.list(new QueryWrapper<TCourse>().lt("end_time", LocalDateTime.now())
                .eq("type", TCourseType.NOT_START.getCode())).forEach(c -> {
            tCourseService.setCourseFinished(c.getId());
        });
    }

    @Test
    @Disabled
    void updateCourseBookedNumber() {
        tCourseService.list(new QueryWrapper<TCourse>()).forEach(c -> {
            tCourseService.updateCourseBookedNumber(c.getId());
        });
    }
}
