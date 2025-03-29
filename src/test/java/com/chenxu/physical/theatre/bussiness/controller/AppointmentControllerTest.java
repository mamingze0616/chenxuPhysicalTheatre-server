package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TCourseOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
    void getAllAppointment() throws Exception {
        // 发送post请求

        logger.info("responseEntity: {}", appointmentController.getAllAppointment());
    }

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
            tAppointmentInfo.setCourseId(841);
            tAppointmentInfo.setUserId(1);

            logger.info(appointmentController.doAppointmentByCourseId("111", tAppointmentInfo).toString());
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
    }

    @Test
    void getAppointmentByUserId() {


        try {
            TAppointmentInfo tAppointmentInfo = new TAppointmentInfo();
            tAppointmentInfo.setCourseId(841);
            tAppointmentInfo.setUserId(1);

            logger.info(appointmentController.getAppointmentByUserId("111", tAppointmentInfo).toString());
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }


    }
}
