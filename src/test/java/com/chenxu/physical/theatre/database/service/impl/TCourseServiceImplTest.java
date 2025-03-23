package com.chenxu.physical.theatre.database.service.impl;

import com.chenxu.physical.theatre.bussiness.controller.CourseController;
import com.chenxu.physical.theatre.bussiness.dto.ApiDateRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


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


    @Test
    void testGetTwoWeekCourses() {
        ApiDateRequest apiDateRequest = new ApiDateRequest();

        logger.info("testGetTwoWeekCourses::{}", courseController.getTwoWeekCourses("dddd", apiDateRequest));
    }


}
