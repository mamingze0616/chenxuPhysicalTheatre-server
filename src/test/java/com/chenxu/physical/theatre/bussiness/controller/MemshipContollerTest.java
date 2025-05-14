package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.bussiness.dto.pay.PayUnifiedOrderResponse;
import com.chenxu.physical.theatre.bussiness.service.PayService;
import com.chenxu.physical.theatre.database.domain.TUserOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(MemshipContollerTest.class);

    @Autowired
    MemshipContoller memshipContoller;
    @Autowired
    PayController payController;
    @Autowired
    PayService payService;

    @Test
    void callback() {
//        JSONObject jsonObject = new JSONObject();
//        logger.info(payController.callback(jsonObject).toString());
//        ;
    }

    @Test
    @Disabled
    void tPayOrderService() {
        TUserOrder tUserOrder = new TUserOrder();
        tUserOrder.setUserId(1);
        tUserOrder.setAmount(1);
        tUserOrder.setPayOrderId(1);

        payService.preCreateMembershipPayOrder(tUserOrder, "测试");


    }

    @Test
    void unifiedOrder() throws JsonProcessingException {
        String responseText = "{ \"errcode\":0, \"errmsg\":\"ok\", \"respdata\":" +
                " {\"return_code\":\"SUCCESS\",\"return_msg\":\"OK\",\"appid\":\"wxd2d16a504f24665e\",\"mch_id\":\"1800008281\",\"sub_appid\":\"wxe1251defc0e44266\",\"sub_mch_id\":\"1716420538\",\"nonce_str\":\"Rf4ql9n1W0owLChr\",\"sign\":\"44C91EF93CF5CB8C92EB5F5345C24B83\",\"result_code\":\"SUCCESS\",\"trade_type\":\"JSAPI\",\"prepay_id\":\"wx141033390744158b737830cfe2e5920001\"," +
                "\"payment\":{\"appId\":\"wxe1251defc0e44266\",\"timeStamp\":\"1747190019\",\"nonceStr\":\"Rf4ql9n1W0owLChr\",\"package\":\"prepay_id=wx141033390744158b737830cfe2e5920001\",\"signType\":\"MD5\",\"paySign\":\"9C13512DD69E246C45079B4A1229B59A\"" +
                "}} }";
        logger.info("text接口返回:[{}]", responseText);
        // 2. 然后手动转换为 PhoneResponse
        ObjectMapper mapper = new ObjectMapper();
        PayUnifiedOrderResponse payUnifiedOrderResponse = mapper.readValue(responseText,
                PayUnifiedOrderResponse.class);
        logger.info("接口返回:[{}]", payUnifiedOrderResponse);
    }


}
