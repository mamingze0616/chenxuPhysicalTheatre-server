package com.chenxu.physical.theatre.bussiness.service;

import com.chenxu.physical.theatre.bussiness.dto.ApiPayCallbackRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mamingze
 * @version 1.0
 * @title PayServiceTest
 * @description
 * @create 2025/5/14 13:29
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class PayServiceTest {
    @Autowired
    PayService payService;

    @Test
    void finishedPayOrder() {
        ApiPayCallbackRequest apiPayCallbackRequest = new ApiPayCallbackRequest();
        apiPayCallbackRequest.setOutTradeNo("34_1_31");
        apiPayCallbackRequest.setReturnCode("SUCCESS");
        payService.finishedPayOrder(apiPayCallbackRequest);

    }
}
