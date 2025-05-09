package com.chenxu.physical.theatre.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenxu.physical.theatre.database.domain.TUserCoupons;

import java.util.List;

/**
 * @author mamingze
 * @description 针对表【T_USER_COUPONS(用户拥有的优惠券的表)】的数据库操作Mapper
 * @createDate 2025-05-06 23:21:48
 * @Entity com.chenxu.physical.theatre.database.domain.TUserCoupons
 */
public interface TUserCouponsMapper extends BaseMapper<TUserCoupons> {
    List<TUserCoupons> getCouponListByUserId(Integer userId);

}




