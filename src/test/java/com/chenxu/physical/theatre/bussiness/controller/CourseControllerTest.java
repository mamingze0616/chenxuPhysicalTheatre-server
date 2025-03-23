package com.chenxu.physical.theatre.bussiness.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author mamingze
 * @version 1.0
 * @title CourseControllerTest
 * @description
 * @create 2025/3/24 00:55
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class CourseControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(CourseControllerTest.class);
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new CourseController()).build();
    }

    @Test
    void getCoursesByID() throws Exception {
        // 发送GET请求
        MockHttpServletResponse responseEntity = this.mockMvc.perform(
                        post("/course/getCoursesByID")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                // 通过content()存放Body的Json
                                .content("{\"id\": \"813\"}"))
                .andReturn().getResponse();
        logger.info("responseEntity: {}", responseEntity.getContentAsString());

    }
}
