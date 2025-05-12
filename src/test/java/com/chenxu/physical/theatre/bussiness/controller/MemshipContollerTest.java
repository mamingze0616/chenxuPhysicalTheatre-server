package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.bussiness.service.PayService;
import com.chenxu.physical.theatre.database.domain.TUserOrder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mamingze
 * @version 1.0
 * @title MemshipContollerTest
 * @description
 * @create 2025/5/12 17:52
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class MemshipContollerTest {
    @Autowired
    MemshipContoller memshipContoller;
    @Autowired
    PayService payService;

    @Test
    @Disabled
    void tPayOrderService() {
        TUserOrder tUserOrder = new TUserOrder();
        tUserOrder.setUserId(1);
        tUserOrder.setAmount(1);
        tUserOrder.setPayOrderId(1);

        payService.preCreateMembershipPayOrder(tUserOrder, "测试");
    }


}
