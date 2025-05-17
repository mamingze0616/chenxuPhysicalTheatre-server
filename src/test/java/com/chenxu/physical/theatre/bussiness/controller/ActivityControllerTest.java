package com.chenxu.physical.theatre.bussiness.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mamingze
 * @version 1.0
 * @title ActivityControllerTest
 * @description
 * @create 2025/5/17 22:22
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class ActivityControllerTest {
    @Autowired
    private ActivityController activityController;

    @Test
    void getActivityList() {
        activityController.getActivityList();
    }
}
