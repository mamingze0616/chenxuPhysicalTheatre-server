package com.chenxu.physical.theatre.bussiness.service;

import com.chenxu.physical.theatre.bussiness.dto.PhoneResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("code", code);
            String responseText = restTemplate.postForObject(url, requestBody, String.class);
            // 2. 然后手动转换为 PhoneResponse
            ObjectMapper mapper = new ObjectMapper();
            PhoneResponse phoneResponse = mapper.readValue(responseText, PhoneResponse.class);
            logger.info("接口返回:[{}]", phoneResponse);
            if (phoneResponse.getErrcode() == 0) {
                return phoneResponse.getPhone_info().getPurePhoneNumber();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取手机号失败:" + e.getMessage());
            throw new RuntimeException("获取手机号失败");
        }
        throw new RuntimeException("获取手机号失败");

    }
}
