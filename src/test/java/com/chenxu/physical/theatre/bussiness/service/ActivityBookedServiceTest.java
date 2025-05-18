package com.chenxu.physical.theatre.bussiness.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mamingze
 * @version 1.0
 * @title ActivityBookedServiceTest
 * @description
 * @create 2025/5/18 08:09
 */

@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class ActivityBookedServiceTest {

    @Autowired
    private ActivityBookedService activityBookedService;

    @Test
    void getActivityListByUserId() {
        activityBookedService.getActivityListByUserId(1);
    }
}
