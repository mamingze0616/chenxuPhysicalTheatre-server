package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.database.domain.TUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mamingze
 * @version 1.0
 * @title PayControllerTest
 * @description
 * @create 2025/5/13 16:38
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class PayControllerTest {
    @Autowired
    PayController payController;

    @Test
    void getPayOrdersByUserId() {
        TUser tUser = new TUser();
        tUser.setId(1);
        payController.getPayOrdersByUserId(tUser);
    }
}
