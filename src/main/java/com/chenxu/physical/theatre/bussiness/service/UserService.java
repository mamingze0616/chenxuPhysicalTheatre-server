package com.chenxu.physical.theatre.bussiness.service;

import com.chenxu.physical.theatre.bussiness.dto.PhoneResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author mamingze
 * @version 1.0
 * @title UserService
 * @description
 * @create 2025/4/7 20:45
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    RestTemplate restTemplate;

    public String getUserPhoneNumber(String code) {
        try {
            String url = "http://api.weixin.qq.com/wxa/business/getuserphonenumber";
            JSONObject param = new JSONObject();
            param.put("code", code);
            PhoneResponse response = restTemplate.postForObject(url, param, PhoneResponse.class);
            logger.info("接口返回:[{}]", response);
            if (response.getErrcode() == 0) {
                return response.getPhone_info().getPurePhoneNumber();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取手机号失败:" + e.getMessage());
            throw new RuntimeException("获取手机号失败");
        }
        throw new RuntimeException("获取手机号失败");

    }
}
