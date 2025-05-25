package com.chenxu.physical.theatre.bussiness.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author mamingze
 * @version 1.0
 * @title CourseSetFinishedScheduledTest
 * @description
 * @create 2025/5/24 17:56
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class CourseSetFinishedScheduledTest {

    @Test
//    @Disabled
    void checkeCourseTypeTask() throws InterruptedException {
        TimeUnit.SECONDS.sleep(600 * 1);
    }
}
