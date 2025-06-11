package com.chenxu.physical.theatre.bussiness.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mamingze
 * @version 1.0
 * @title SubscribeMessageService
 * @description
 * @create 2025/5/25 16:09
 */
@Service
public class SubscribeMessageService {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeMessageService.class);
    @Value("${wx.url.subscribeMessage}")
    private String subscribeMessageUrl;

    @Value("${wx.message.template.bookedsuccess.id}")
    private String bookedSuccessTemplateId;

    @Value("${wx.message.template.bookedcancel.id}")
    private String bookedCancelTemplateId;

    @Autowired
    RestClient restClient;


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");

    /**
     * 发送订阅消息
     *
     * @param openid
     * @param templateId
     * @param page
     * @param data
     * @return
     */
    public boolean sendSubscribeMessage(String openid,
                                        String templateId,
                                        String page,
                                        Map<String, Object> data) {
        logger.info("发送订阅消息 templateId:{},openid:{}", templateId, openid);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("touser", openid);
        requestBody.put("template_id", templateId);
        requestBody.put("page", page);
        requestBody.put("data", data);
        logger.info("发送订阅消息 data:{}", data);
        HttpEntity<Map> entity = new HttpEntity<>(requestBody, headers);

        String responseText = restClient.post().uri(subscribeMessageUrl)
                .body(requestBody)
                .retrieve().body(String.class);
        logger.info("发送订阅消息接口返回:[{}]", responseText);
        return true;
    }

    /**
     * 发送预约成功消息
     *
     * @param openid
     * @param courseName
     * @param courseStartTime
     * @param orderTime
     * @return
     */
    public boolean sendBookedSuccessMessage(String openid, String courseName, LocalDateTime courseStartTime, LocalDateTime orderTime) {
        Map<String, Object> data = new HashMap<>();
        //课程名称
        Map<String, Object> thing41 = new HashMap<>();
        thing41.put("value", courseName);
        data.put("thing41", thing41);
        //课程时间
        Map<String, Object> time43 = new HashMap<>();
        time43.put("value", courseStartTime.format(formatter));
        data.put("time43", time43);

        //课程时间
        Map<String, Object> time73 = new HashMap<>();
        time73.put("value", orderTime.format(formatter));
        data.put("time73", time73);

        return sendSubscribeMessage(openid, bookedSuccessTemplateId, "pages/index/index", data);
    }

    public boolean sendBookedCancelMessage(String openid, String courseName,
                                           LocalDateTime courseStartTime,
                                           String reason, String tips) {
        Map<String, Object> data = new HashMap<>();
        //课程名称
        Map<String, Object> thing28 = new HashMap<>();
        thing28.put("value", courseName);
        data.put("thing28", thing28);
        //课程时间
        Map<String, Object> time30 = new HashMap<>();
        time30.put("value", courseStartTime.format(formatter));
        data.put("time30", time30);

        //原因
        Map<String, Object> thing4 = new HashMap<>();
        thing4.put("value", reason);
        data.put("thing4", thing4);
        //提示
        Map<String, Object> thing9 = new HashMap<>();
        thing9.put("value", tips);
        data.put("thing9", thing9);
        try {
            return sendSubscribeMessage(openid, bookedCancelTemplateId, "pages/index/index", data);
        } catch (Exception e) {
            logger.error("发送预约取消消息失败:{}", e.getMessage());
        }
        return false;

    }
}
