package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.database.constant.TCouponType;
import com.chenxu.physical.theatre.database.domain.TCoupon;
import com.chenxu.physical.theatre.database.domain.TUserCoupons;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

/**
 * @author mamingze
 * @version 1.0
 * @title CouponControllerTest
 * @description
 * @create 2025/5/7 00:12
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class CouponControllerTest {
    @Autowired
    CouponController couponController;

    @Test
    void add() {
        TCoupon tCoupon = new TCoupon();
        tCoupon.setCourseNumber(1);
        tCoupon.setDiscount(80);
        tCoupon.setStartTime(LocalDate.now());
        tCoupon.setTitle("免费体验课");
        tCoupon.setType(TCouponType.COUPON_TYPE_COURSE);
        tCoupon.setValidityPeriod(60);
        couponController.add(tCoupon);
    }

    @Test
    void getCouponList() {
    }

    @Test
    void ississueCoupon() {
        TUserCoupons tUserCoupons = new TUserCoupons();
        tUserCoupons.setCouponId(10);
        tUserCoupons.setUserId(1);
        tUserCoupons.setEffectiveDays(10);
        couponController.ississueCoupon(tUserCoupons);
    }
}
