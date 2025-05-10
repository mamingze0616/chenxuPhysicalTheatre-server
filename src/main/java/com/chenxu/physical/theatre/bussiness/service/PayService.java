package com.chenxu.physical.theatre.bussiness.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mamingze
 * @version 1.0
 * @title PayService
 * @description
 * @create 2025/5/11 02:33
 */
@Service
public class PayService {
    private static final Logger logger = LoggerFactory.getLogger(PayService.class);
    @Value("${pay.unifiedOrder.url}")
    private String unifiedOrderUrl;
    @Value("${wx.env}")
    private String env;
    @Value("${wx.mch}")
    private String mch;
    @Value("${pay.unifiedOrder.callback.service}")
    private String service;
    @Value("${pay.unifiedOrder.callback.path}")
    private String path;

    @Autowired
    RestTemplate restTemplate;

    /**
     * @param openid
     * @param body
     * @param outTradeNo 内部订单号
     * @param totalFee
     * @return
     */
    public String unifiedOrder(String openid, String body, String outTradeNo, int totalFee, String spbillCreateIp) {

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("body", body);
            requestBody.put("openid", openid);
            requestBody.put("out_trade_no", outTradeNo);
            requestBody.put("spbill_create_ip", spbillCreateIp);
            requestBody.put("env_id", env);
            requestBody.put("sub_mch_id", mch);
            requestBody.put("total_fee", totalFee);
            requestBody.put("callback_type", 2);
            Map<String, String> container = new HashMap<>();
            container.put("service", service);
            container.put("path", path);
            requestBody.put("container", container);
            logger.info("接口请求:[{}]", requestBody);
            String responseText = restTemplate.postForObject(unifiedOrderUrl, requestBody, String.class);
            logger.info("接口返回:[{}]", responseText);
            return responseText;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("统一下单失败:" + e.getMessage());
            throw new RuntimeException("统一下单失败");
        }

    }

}
