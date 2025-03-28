package com.chenxu.physical.theatre.bussiness.controller;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mamingze
 * @version 1.0
 * @title UserControllerTest
 * @description
 * @create 2025/3/28 17:42
 */
@SpringBootTest
class UserControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);
    @Autowired
    UserController userController;
//    MockMvc mockMvc;
//    @BeforeEach
//    void setUp() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
//    }

    @Test
    void getAllUser() throws Exception {
//        mockMvc.perform(post("/user/getAllUser")).andDo(print());
        logger.info(userController.getAllUser().toString());
//        userController.getAllUser();
    }
}
