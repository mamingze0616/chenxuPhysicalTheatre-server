package com.chenxu.physical.theatre.bussiness.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.database.constant.TActivityBookedInfoStatusEnum;
import com.chenxu.physical.theatre.database.constant.TPayOrderType;
import com.chenxu.physical.theatre.database.constant.TUserCouponsStatus;
import com.chenxu.physical.theatre.database.domain.TActivityBookedInfo;
import com.chenxu.physical.theatre.database.domain.TPayOrder;
import com.chenxu.physical.theatre.database.domain.TUserCoupons;
import com.chenxu.physical.theatre.database.service.TActivityBookedInfoService;
import com.chenxu.physical.theatre.database.service.TUserCouponsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mamingze
 * @version 1.0
 * @title ActivityBookedService
 * @description
 * @create 2025/5/18 06:32
 */
@Service
public class ActivityBookedService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityBookedService.class);
    @Autowired
    TUserCouponsService tUserCouponsService;
    @Autowired
    TActivityBookedInfoService tActivityBookedInfoService;
    @Autowired
    PayService payService;

    public boolean checkCanBooked(TActivityBookedInfo tActivityBookedInfo) {
        //查看优惠券是否已经用
        try {
            //查询用户优惠券,暂时只能使用一个
            logger.info("查询用户优惠券:" + tActivityBookedInfo.getCouponId());
            TUserCoupons userCoupons = tUserCouponsService.getById(tActivityBookedInfo.getCouponId());
            //判断是否已经使用,没查出来优惠券就跳过
            if (userCoupons.getStatus() == TUserCouponsStatus.FINISHED) {
                throw new RuntimeException("优惠券已经使用");
            }
            //判断是否已经预约过该活动
            List<TActivityBookedInfo> tActivityBookedInfoList = tActivityBookedInfoService.list(new QueryWrapper<TActivityBookedInfo>()
                    .eq("user_id", tActivityBookedInfo.getUserId())
                    .eq("activity_id", tActivityBookedInfo.getActivityId())
                    .ne("status", TActivityBookedInfoStatusEnum.UNPAYED.getCode()));
            if (tActivityBookedInfoList.size() > 0) {
                throw new RuntimeException("已经预约过该活动");
            }
            return true;
        } catch (Exception e) {
            throw e;
        }

    }

    public TPayOrder preBooked(TActivityBookedInfo tActivityBookedInfo, String spbillCreateIp) {
        //获取下单用户的openid
        TPayOrder payOrder = new TPayOrder();
        try {
            //第一步:预创建一个空白支付订单
            TPayOrder prePayOrder = payService.preBooked(tActivityBookedInfo,
                    "预约活动");
            //创建一个用户升级订单,绑定预创建的支付订单
            tActivityBookedInfo.setPayOrderId(prePayOrder.getId());
            tActivityBookedInfo.setStatus(TActivityBookedInfoStatusEnum.UNPAYED);
            //第二步:保存用户升级订单
            tActivityBookedInfoService.save(tActivityBookedInfo);
            //第三步:给微信官网发送请求，获取预支付订单
            payOrder = payService.unifiedOrder(prePayOrder, tActivityBookedInfo.getId(), TPayOrderType.ACTIVITY, spbillCreateIp);
            payOrder.setActivityBookedInfo(tActivityBookedInfo);
            tUserCouponsService.lambdaUpdate()
                    .eq(TUserCoupons::getId, tActivityBookedInfo.getCouponId())
                    .set(TUserCoupons::getStatus, TUserCouponsStatus.FINISHED.getCode())
                    .update();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return payOrder;
        }

    }
}
