package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiPayCallbackRequest;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.service.PayService;
import com.chenxu.physical.theatre.database.domain.TUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mamingze
 * @version 1.0
 * @title PayController
 * @description
 * @create 2025/5/11 03:59
 */
@RestController
@RequestMapping("/pay")
public class PayController {
    private static final Logger logger = LoggerFactory.getLogger(PayController.class);
    @Autowired
    private PayService payService;

    @PostMapping("/callback")
    public JSONObject callback(@RequestBody ApiPayCallbackRequest apiPayCallbackRequest) {
        logger.info("callback:{}", apiPayCallbackRequest);
        JSONObject result = new JSONObject();
        try {
            payService.finishedPayOrder(apiPayCallbackRequest);
            result.put("return_code", "SUCCESS");
            result.put("return_msg", "OK");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            return result;
        }
    }

    @PostMapping("/getPayOrdersByUserId")
    public ApiResponse getPayOrdersByUserId(@RequestBody TUser tUser) {
        logger.info("getPayOrdersByUserId:: user = [{}]", tUser);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            apiResponse.setData(payService.getPayOrdersByUserId(tUser));
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;

    }

}
