package com.chenxu.physical.theatre.bussiness.service;

import com.chenxu.physical.theatre.database.service.TCourseOrderService;
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
    @Autowired
    TCourseOrderService courseOrderService;

    @Test
    void getUnWriteOffCourseOrderSpilt() {

        courseOrderSplitService.getUnWriteOffCourseOrderSpilt(1);
    }

    @Test
    void updateAllUnWriteOffCourseOrderSpiltStatus() {
        courseOrderSplitService.updateAllUnWriteOffCourseOrderSpiltStatus();
    }

    @Test
    void splitCourseOrder() {
        courseOrderSplitService.splitCourseOrder(courseOrderService.getById(3));
    }
}
