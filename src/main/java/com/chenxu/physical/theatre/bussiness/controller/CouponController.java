package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.database.domain.TCoupon;
import com.chenxu.physical.theatre.database.service.TCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mamingze
 * @version 1.0
 * @title CouponController
 * @description
 * @create 2025/5/6 16:16
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {
    Logger logger = LoggerFactory.getLogger(CouponController.class);
    @Autowired
    private TCouponService tCouponService;

    @PostMapping("/add")
    public ApiResponse add(@RequestBody TCoupon tCoupon) {
        logger.info("add::tCoupon = [{}]", tCoupon);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            //新增优惠券
            tCouponService.save(tCoupon);
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
        } catch (Exception e) {
            logger.error("add::error = [{}]", e.getMessage());
            apiResponse.setErrorMsg("添加失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }

    //获取优惠券列表
    @PostMapping("/getCouponList")
    public ApiResponse getCouponList(@RequestBody TCoupon tCoupon) {
        logger.info("getCouponList::tCoupon = [{}]", tCoupon);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            //获取优惠券列表
            apiResponse.setData(tCouponService.list());
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
        } catch (Exception e) {
            logger.error("getCouponList::error = [{}]", e.getMessage());
            apiResponse.setErrorMsg("获取失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }
}
