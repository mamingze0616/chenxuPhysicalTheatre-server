package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.database.domain.TCourseOrder;
import com.chenxu.physical.theatre.database.domain.TUser;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author mamingze
 * @version 1.0
 * @title UserCourseOrderControllerTest
 * @description
 * @create 2025/3/28 16:27
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class UserCourseOrderControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(UserCourseOrderControllerTest.class);

    MockMvc mockMvc;

    @Autowired
    UserCourseOrderController userCourseOrderController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserCourseOrderController()).build();
    }


    @Test
    void updateCourseOrder() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("userId", 22);
        jsonObject.put("courseNumber", 1);
        jsonObject.put("type", 1);
        jsonObject.put("operatorId", 1);
        mockMvc.perform(post("/order/course/update")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObject.toString())).andDo(print());
    }

    @Test
    void queryOrderByUserId() throws Exception {
        TUser tUser = new TUser();
        tUser.setId(10);
        userCourseOrderController.queryOrderByUserId(tUser);
    }

    @Test
    void successToCheck() {
        TCourseOrder test = new TCourseOrder();
        test.setId(1);
        userCourseOrderController.successToCheck(test);
    }
}
