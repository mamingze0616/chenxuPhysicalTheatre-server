package com.chenxu.physical.theatre.bussiness.service;

import com.chenxu.physical.theatre.database.domain.TUser;
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
    void splitCourseOrder() {
        TUser user = new TUser();
        user.setId(1);
        courseOrderSplitService.splitCourseOrder(user);
    }

    @Test
    void getUnWriteOffCourseOrderSpilt() {

        courseOrderSplitService.getUnWriteOffCourseOrderSpilt(1);
    }
}
