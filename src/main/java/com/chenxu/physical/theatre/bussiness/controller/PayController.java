package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.dto.pay.ApiUnifiedOrderRequest;
import com.chenxu.physical.theatre.bussiness.service.PayService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

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
    public JSONObject callback(@RequestBody JSONObject jsonObject) {
        logger.info("callback:{}", jsonObject);
        JSONObject result = new JSONObject();
        try {
            
            result.put("return_code", "SUCCESS");
            result.put("return_msg", "OK");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            return result;
        }

    }

    @PostMapping("/unifiedOrder")
    public ApiResponse unifiedOrder(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                    String openid, @RequestBody ApiUnifiedOrderRequest apiUnifiedOrderRequest, HttpServletRequest request) {

        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        logger.info("openid:{},ipAddress:{},apiUnifiedOrderRequest:{}", openid, ipAddress, apiUnifiedOrderRequest);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(payService.unifiedOrder(openid, apiUnifiedOrderRequest.getBody(),
                    apiUnifiedOrderRequest.getOutTradeNo(), apiUnifiedOrderRequest.getTotalFee(), ipAddress));
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg("下单失败");

        }
        return apiResponse;
    }

}
