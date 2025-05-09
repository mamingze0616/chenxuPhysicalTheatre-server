package com.chenxu.physical.theatre.database.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenxu.physical.theatre.database.domain.TUserCoupons;

import java.util.List;

/**
 * @author mamingze
 * @description 针对表【T_USER_COUPONS(用户拥有的优惠券的表)】的数据库操作Service
 * @createDate 2025-05-06 23:21:48
 */
public interface TUserCouponsService extends IService<TUserCoupons> {
    List<TUserCoupons> getCouponListByUserId(Integer userId);
}
