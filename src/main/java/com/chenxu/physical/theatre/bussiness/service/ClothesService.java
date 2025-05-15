package com.chenxu.physical.theatre.bussiness.service;

import com.chenxu.physical.theatre.database.constant.TPayOrderType;
import com.chenxu.physical.theatre.database.constant.TUserOrderStatus;
import com.chenxu.physical.theatre.database.domain.TClothesOrder;
import com.chenxu.physical.theatre.database.domain.TPayOrder;
import com.chenxu.physical.theatre.database.service.TClothesOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mamingze
 * @version 1.0
 * @title ClothesService
 * @description
 * @create 2025/5/16 02:39
 */
@Service
public class ClothesService {
    private static final Logger logger = LoggerFactory.getLogger(ClothesService.class);
    @Autowired
    PayService payService;
    @Autowired
    TClothesOrderService tClothesOrderService;

    public TPayOrder preBuyClothes(TClothesOrder tClothesOrder, String spbillCreateIp) {
        //获取下单用户的openid
        TPayOrder payOrder = new TPayOrder();
        try {
            //第一步:预创建一个空白支付订单
            TPayOrder prePayOrder = payService.preCreateClothesPayOrder(tClothesOrder,
                    "购买形体服");
            //创建一个用户升级订单,绑定预创建的支付订单
            tClothesOrder.setPayOrderId(prePayOrder.getId());
            tClothesOrder.setStatus(TUserOrderStatus.UNPAID);
            //第二步:保存用户升级订单
            tClothesOrderService.save(tClothesOrder);
            //第三步:给微信官网发送请求，获取预支付订单
            payOrder = payService.unifiedOrder(prePayOrder,
                    tClothesOrder.getId(), TPayOrderType.CLOTHES, spbillCreateIp);
            payOrder.setClothesOrder(tClothesOrder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            return payOrder;
        }

    }

    public TClothesOrder successToBuyClothes(TClothesOrder tClothesOrder) {
        try {
            TClothesOrder clothesOrder = tClothesOrderService.getById(tClothesOrder.getId());
            if (clothesOrder == null) {
                throw new RuntimeException("用户形体服订单不存在");
            }
            //更新订单状态为已支付
            tClothesOrderService.lambdaUpdate().eq(TClothesOrder::getId, tClothesOrder.getId())
                    .set(TClothesOrder::getStatus, TUserOrderStatus.PAID.getCode()).update();
            //修改用户状态为会员用户
            return tClothesOrder;
            //查询用户升级订单
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
