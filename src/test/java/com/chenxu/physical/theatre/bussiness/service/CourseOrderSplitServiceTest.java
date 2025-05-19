package com.chenxu.physical.theatre.bussiness.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mamingze
 * @version 1.0
 * @title CourseOrderSplitServiceTest
 * @description
 * @create 2025/5/19 01:08
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class CourseOrderSplitServiceTest {
    @Autowired
    CourseOrderSplitService courseOrderSplitService;

    @Test
    void getUnWriteOffCourseOrderSpilt() {

        courseOrderSplitService.getUnWriteOffCourseOrderSpilt(1);
    }
}
