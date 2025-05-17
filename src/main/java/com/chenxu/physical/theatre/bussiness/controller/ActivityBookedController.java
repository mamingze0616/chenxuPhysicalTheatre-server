package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.service.ActivityBookedService;
import com.chenxu.physical.theatre.bussiness.util.IpUtils;
import com.chenxu.physical.theatre.database.domain.TActivityBookedInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author mamingze
 * @version 1.0
 * @title ActivityBookedController
 * @description
 * @create 2025/5/18 06:30
 */
@RestController
@RequestMapping("/activity/booked")
public class ActivityBookedController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityBookedController.class);
    @Autowired
    ActivityBookedService service;

    /**
     * 升级成为会员,下单阶段
     *
     * @return
     */
    @PostMapping("/preBooked")
    public ApiResponse preBooked(@RequestBody TActivityBookedInfo tActivityBookedInfo, HttpServletRequest request) {
        logger.info("preBooked::tActivityBookedInfo = [{}]", tActivityBookedInfo);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(tActivityBookedInfo.getUserId()).orElseThrow(() -> new RuntimeException("用户id为空"));
            Optional.ofNullable(tActivityBookedInfo.getAmount()).orElseThrow(() -> new RuntimeException("金额不能为空"));
            //判断用户是否是会员,已经是会员则不用升级了,判断优惠卷是否有效
            if (service.checkCanBooked(tActivityBookedInfo)) {
                //要下一个会员升级订单的话要创建一个支付订单
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(service.preBooked(tActivityBookedInfo, IpUtils.getIp(request)));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;


    }

}
