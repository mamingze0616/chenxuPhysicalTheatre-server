package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.domain.TCourseOrder;
import com.chenxu.physical.theatre.database.domain.TUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

/**
 * @author mamingze
 * @version 1.0
 * @title AppointmentControllerTest
 * @description
 * @create 2025/3/27 13:39
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class AppointmentControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentControllerTest.class);
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AppointmentController()).build();
    }

    @Autowired
    AppointmentController appointmentController;

    @Test
    void getAppointmentInfoByCourseId() {
        try {
            TAppointmentInfo appointmentInfo = new TAppointmentInfo();
            appointmentInfo.setCourseId(841);
            logger.info(appointmentController.getAppointmentInfoByCourseId("111", appointmentInfo).toString());
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
    }

    @Test
    void getOverviewOfCourseNumberInfoAndAppointmentInfo() {
        try {
            TCourseOrder courseOrder = new TCourseOrder();
            courseOrder.setUserId(1);
            logger.info(appointmentController.getOverviewOfCourseNumberInfoAndAppointmentInfo("111", courseOrder).toString());
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
    }

    @Test
    void doAppointmentByCourseId() {
        try {
            TAppointmentInfo tAppointmentInfo = new TAppointmentInfo();
            tAppointmentInfo.setCourseId(883);
            tAppointmentInfo.setUserId(1);

            logger.info(appointmentController.doAppointmentByCourseId("111", tAppointmentInfo).toString());
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
    }

    @Test
    void getAppointmentInfosByUserIdAndDate() {


        try {
            TAppointmentInfo tAppointmentInfo = new TAppointmentInfo();
            tAppointmentInfo.setCourseId(841);
            tAppointmentInfo.setUserId(1);
            tAppointmentInfo.setDate(LocalDate.now().plusDays(30));

            logger.info(appointmentController.getAppointmentInfosByUserIdAndDate("111", tAppointmentInfo).toString());
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }


    }

    @Test
    void getBookableCoursesByUseridAndDate() {
        try {
            TAppointmentInfo tAppointmentInfo = new TAppointmentInfo();
            tAppointmentInfo.setCourseId(841);
            tAppointmentInfo.setUserId(1);
            tAppointmentInfo.setDate(LocalDate.now().plusDays(30));

            logger.info(appointmentController.getBookableCoursesByUseridAndDate("111", tAppointmentInfo).toString());
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }

    }

    @Test
    void cancelCourseAppointment() {
        try {
            TAppointmentInfo tAppointmentInfo = new TAppointmentInfo();
            tAppointmentInfo.setCourseId(883);
            tAppointmentInfo.setUserId(1);

            logger.info(appointmentController.cancelCourseAppointment("111", tAppointmentInfo).toString());
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
    }

    @Test
    void getCourseInfoWithAppointmentInfoList() {
        try {
            TCourse tCourse = new TCourse();
            tCourse.setCurrent(3);
            tCourse.setSize(10);
            tCourse.setDate(LocalDate.now());
            logger.info(appointmentController.getCourseInfoWithAppointmentInfoList("111", tCourse).toString());
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
    }

    @Test
    void getUserInfoWithAppointmentInfoList() {
        try {
            TUser tUser = new TUser();
            tUser.setCurrent(1);
            tUser.setSize(10);
//            tUser.setType(TUser.TUserType.ADMIN);
            logger.info(appointmentController.getUserInfoWithAppointmentInfoList("111", tUser).toString());
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
    }

}
