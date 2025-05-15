package com.chenxu.physical.theatre.bussiness.service;

import com.chenxu.physical.theatre.database.constant.TPayOrderType;
import com.chenxu.physical.theatre.database.constant.TUserCouponsStatus;
import com.chenxu.physical.theatre.database.constant.TUserOrderStatus;
import com.chenxu.physical.theatre.database.constant.TUserType;
import com.chenxu.physical.theatre.database.domain.TPayOrder;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.domain.TUserCoupons;
import com.chenxu.physical.theatre.database.domain.TUserOrder;
import com.chenxu.physical.theatre.database.service.TUserCouponsService;
import com.chenxu.physical.theatre.database.service.TUserOrderService;
import com.chenxu.physical.theatre.database.service.TUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mamingze
 * @version 1.0
 * @title MemberShipService
 * @description
 * @create 2025/5/12 16:21
 */
@Service
public class MemberShipService {
    private static final Logger logger = LoggerFactory.getLogger(MemberShipService.class);

    @Autowired
    PayService payService;
    @Autowired
    TUserCouponsService tUserCouponsService;
    @Autowired
    TUserService tUserService;
    @Autowired
    TUserOrderService tUserOrderService;

    public TPayOrder preUpgrade(TUserOrder tUserOrder, String spbillCreateIp) {
        //获取下单用户的openid
        TPayOrder payOrder = new TPayOrder();
        try {
            //第一步:预创建一个空白支付订单
            TPayOrder prePayOrder = payService.preCreateMembershipPayOrder(tUserOrder,
                    "升级会员订单");
            //创建一个用户升级订单,绑定预创建的支付订单
            tUserOrder.setPayOrderId(prePayOrder.getId());
            tUserOrder.setStatus(TUserOrderStatus.UNPAID);
            //第二步:保存用户升级订单
            tUserOrderService.save(tUserOrder);
            //第三步:给微信官网发送请求，获取预支付订单
            payOrder = payService.unifiedOrder(prePayOrder,
                    tUserOrder.getId(), TPayOrderType.MEMBERSHIP, spbillCreateIp);
            tUserCouponsService.lambdaUpdate().eq(TUserCoupons::getId, tUserOrder.getCouponIds())
                    .set(TUserCoupons::getStatus, TUserCouponsStatus.FINISHED.getCode()).update();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return payOrder;
        }

    }


    public TUserOrder successToUpgrade(TUserOrder tUserOrder) {
        try {
            //查询用户升级订单
            TUserOrder userOrder = tUserOrderService.getById(tUserOrder.getId());
            if (userOrder == null) {
                throw new RuntimeException("用户升级订单不存在");
            }
            //更新订单状态为已支付
            tUserOrderService.lambdaUpdate().eq(TUserOrder::getId, tUserOrder.getId())
                    .set(TUserOrder::getStatus, TUserOrderStatus.PAID.getCode()).update();
            //修改用户状态为会员用户
            tUserService.lambdaUpdate().eq(TUser::getId, userOrder.getUserId())
                    .set(TUser::getType, TUserType.MEMBER.getCode()).update();
            return userOrder;
            //查询用户升级订单
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
