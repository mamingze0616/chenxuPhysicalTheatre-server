package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiIssueCouponRequest;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.database.constant.TUserCouponsStatus;
import com.chenxu.physical.theatre.database.domain.TCoupon;
import com.chenxu.physical.theatre.database.domain.TUserCoupons;
import com.chenxu.physical.theatre.database.service.TCouponService;
import com.chenxu.physical.theatre.database.service.TUserCouponsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

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
    @Autowired
    private TUserCouponsService tUserCouponsService;

    @PostMapping("/add")
    public ApiResponse add(@RequestBody TCoupon tCoupon) {
        logger.info("add::tCoupon = [{}]", tCoupon);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            //新增优惠券
            //当前日期在5月20号之前
            if (LocalDate.now().isBefore(LocalDate.of(2025, 5, 20))) {
                tCoupon.setStartTime(LocalDate.of(2025, 5, 20));
            }
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

    @PostMapping("/getCouponListByUserId")
    public ApiResponse getCouponListByUserId(@RequestBody TUserCoupons tUserCoupons) {
        logger.info("getCouponList::tUserCoupons = [{}]", tUserCoupons);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            //获取优惠券列表
            Optional.ofNullable(tUserCoupons.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));

            apiResponse.setData(tUserCouponsService.getCouponListByUserId(tUserCoupons.getUserId()));
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
        } catch (Exception e) {
            logger.error("getCouponList::error = [{}]", e.getMessage());
            apiResponse.setErrorMsg("获取失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }

    //用户领取优惠券
    @PostMapping("/userGetCoupon")
    public ApiResponse userGetCoupon(@RequestBody TUserCoupons tUserCoupons) {
        logger.info("userGetCoupon::TUserCoupons = [{}]", tUserCoupons);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            //用户领取优惠券
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
        } catch (Exception e) {
            logger.error("userGetCoupon::error = [{}]", e.getMessage());
            apiResponse.setErrorMsg("获取失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }

    //给制定用户下发优惠券
    @PostMapping("/issueCoupon")
    public ApiResponse ississueCoupon(@RequestBody TUserCoupons tUserCoupons) {
        logger.info("issueCoupon::TUserCoupons = [{}]", tUserCoupons);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            //下发优惠券
            Optional.ofNullable(tUserCoupons.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));
            Optional.ofNullable(tUserCoupons.getCouponId()).orElseThrow(() -> new RuntimeException("couponId为空"));
            if (tCouponService.lambdaQuery().eq(TCoupon::getId, tUserCoupons.getCouponId()).count() == 0) {
                throw new RuntimeException("优惠券不存在");
            }
            //当前日期在5月20号之前
            if (LocalDate.now().isBefore(LocalDate.of(2025, 5, 20))) {
                tUserCoupons.setIssueTime(LocalDate.of(2025, 5, 20));
            } else {
                tUserCoupons.setIssueTime(LocalDate.now());
            }
            //初始状态未使用
            tUserCoupons.setStatus(TUserCouponsStatus.NOT_USE);
            tUserCouponsService.save(tUserCoupons);
            apiResponse.setData(tUserCoupons);
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
        } catch (Exception e) {
            logger.error("issueCoupon::error = [{}]", e.getMessage());
            apiResponse.setErrorMsg("获取失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }


    //批量下发优惠券
    @PostMapping("/batchIssuseCoupon")
    public ApiResponse batchIssuseCoupon(@RequestBody ApiIssueCouponRequest apiIssueCouponRequest) {
        logger.info("batchIssuseCoupon::ApiIssueCouponRequest = [{}]", apiIssueCouponRequest);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            //判空
            Optional.ofNullable(apiIssueCouponRequest.getUserIds()).orElseThrow(() -> new RuntimeException("userIds为空"));
            Optional.ofNullable(apiIssueCouponRequest.getCouponId()).orElseThrow(() -> new RuntimeException("couponId为空"));
            //获取优惠券
            TCoupon tCoupon = tCouponService.getById(apiIssueCouponRequest.getCouponId());
            //按照;分割
            String[] userIds = apiIssueCouponRequest.getUserIds().split(";");
            for (String userId : userIds) {
                TUserCoupons tUserCoupons = new TUserCoupons();
                tUserCoupons.setUserId(Integer.parseInt(userId));
                tUserCoupons.setCouponId(apiIssueCouponRequest.getCouponId());
                tUserCoupons.setStatus(TUserCouponsStatus.NOT_USE);
                if (LocalDate.now().isBefore(LocalDate.of(2025, 5, 20))) {
                    tUserCoupons.setIssueTime(LocalDate.of(2025, 5, 20));
                } else {
                    tUserCoupons.setIssueTime(LocalDate.now());
                }
                tUserCouponsService.save(tUserCoupons);
            }

        } catch (Exception e) {
            logger.error("batchIssuseCoupon::error = [{}]", e.getMessage());
            apiResponse.setErrorMsg("下发失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }

}
