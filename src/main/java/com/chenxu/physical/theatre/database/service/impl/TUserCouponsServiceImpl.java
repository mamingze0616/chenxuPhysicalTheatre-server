package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxu.physical.theatre.database.domain.TUserCoupons;
import com.chenxu.physical.theatre.database.mapper.TUserCouponsMapper;
import com.chenxu.physical.theatre.database.service.TUserCouponsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mamingze
 * @description 针对表【T_USER_COUPONS(用户拥有的优惠券的表)】的数据库操作Service实现
 * @createDate 2025-05-06 23:21:48
 */
@Service
public class TUserCouponsServiceImpl extends ServiceImpl<TUserCouponsMapper, TUserCoupons>
        implements TUserCouponsService {

    @Override
    public List<TUserCoupons> getCouponListByUserId(Integer userId) {
        return baseMapper.getCouponListByUserId(userId);
    }
}




