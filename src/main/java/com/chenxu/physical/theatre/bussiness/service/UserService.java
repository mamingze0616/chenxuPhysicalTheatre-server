package com.chenxu.physical.theatre.bussiness.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.dto.PhoneResponse;
import com.chenxu.physical.theatre.database.constant.TCourseOrderSpiltStatusEnum;
import com.chenxu.physical.theatre.database.domain.TCourseOrderSpilt;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.domain.TUserCoupons;
import com.chenxu.physical.theatre.database.service.TCourseOrderSpiltService;
import com.chenxu.physical.theatre.database.service.TUserCouponsService;
import com.chenxu.physical.theatre.database.service.TUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
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
    @Value("${wx.url.getPhoneNumber}")
    private String getPhoneNumber;
    @Autowired
    TUserCouponsService tUserCouponsService;
    @Autowired
    TUserService tUserService;
    @Autowired
    TCourseOrderSpiltService tCourseOrderSpiltService;

    public String getUserPhoneNumber(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("code", code);
            HttpEntity<Map> entity = new HttpEntity<>(requestBody, headers);
            String responseText = restTemplate.postForObject(getPhoneNumber, entity, String.class);
            logger.info("接口返回:[{}]", responseText);
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

    public TUser getUserCardInfo(TUser tUser) {
        try {
            tUser.setUserCoupons(tUserCouponsService.list(new QueryWrapper<TUserCoupons>().eq("user_id", tUser.getId())));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取用户卡信息失败:" + e.getMessage());
            throw new RuntimeException("获取用户卡信息失败");
        }
        return tUser;
    }

    public boolean updateEffectiveCourseCount(Integer userId) {
        try {
            TUser user = tUserService.getById(userId);
            //查询有效的课程订单
            long writeOFFnumber = tCourseOrderSpiltService.list(new QueryWrapper<TCourseOrderSpilt>()
                    .eq("status", TCourseOrderSpiltStatusEnum.UNWRITE_OFF.getCode())).stream().count();
            user.setEffectiveCourseCount((int) writeOFFnumber);
            return tUserService.updateById(user);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新用户有效课程数量失败:" + e.getMessage());
            throw new RuntimeException("更新用户有效课程数量失败");
        }
    }

    public boolean loginUpdateUserInfo(TUser tUser) {
        try {
            tUser.setLoginAt(LocalDateTime.now());
            //计算有效课程数量(包括未核销和已核销的课程,除去已过期的信息)
            long writeOFFnumber = tCourseOrderSpiltService.list(new QueryWrapper<TCourseOrderSpilt>()
                    .in("status", TCourseOrderSpiltStatusEnum.UNWRITE_OFF.getCode()
                            , TCourseOrderSpiltStatusEnum.WRITE_OFF.getCode())).stream().count();
            tUser.setEffectiveCourseCount((int) writeOFFnumber);
            return tUserService.updateById(tUser);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新用户信息失败:" + e.getMessage());
            throw new RuntimeException("更新用户信息失败");
        }

    }


}
