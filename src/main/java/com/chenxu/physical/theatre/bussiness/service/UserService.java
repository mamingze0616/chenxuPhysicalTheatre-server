package com.chenxu.physical.theatre.bussiness.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.dto.PhoneResponse;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.domain.TUserCoupons;
import com.chenxu.physical.theatre.database.service.TUserCouponsService;
import com.chenxu.physical.theatre.database.service.TUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author mamingze
 * @version 1.0
 * @title UserService
 * @description
 * @create 2025/4/7 20:45
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    TUserCouponsService tUserCouponsService;
    @Autowired
    TUserService tUserService;
    @Autowired
    CourseOrderSplitService courseOrderSplitService;

    @Autowired
    WeiXinContainerServie weiXinContainerServie;

    public TUser getById(Integer id) {
        return tUserService.getById(id);
    }

    public List<TUser> getAllUser() {
        return tUserService.list();
    }

    public String getUserPhoneNumber(String code) {
        try {
            PhoneResponse phoneResponse = weiXinContainerServie.getUserPhoneNumber(code);
            logger.info("接口返回:[{}]", phoneResponse);
            if (phoneResponse.getErrcode() == 0) {
                return phoneResponse.getPhone_info().getPurePhoneNumber();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取手机号失败:" + e.getMessage());
            throw new RuntimeException("获取手机号失败");
        }
        throw new RuntimeException("获取手机号失败");

    }

    public TUser getUserCardInfo(TUser tUser) {
        try {
            tUser.setUserCoupons(tUserCouponsService.list(new QueryWrapper<TUserCoupons>().eq("user_id", tUser.getId())));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取用户卡信息失败:" + e.getMessage());
            throw new RuntimeException("获取用户卡信息失败");
        }
        return tUser;
    }

    /**
     * 更新有效课程数量
     *
     * @param userId
     * @return
     */
    public boolean updateEffectiveCourseCount(Integer userId) {
        try {

            return tUserService.lambdaUpdate().set(TUser::getEffectiveCourseCount, (int) courseOrderSplitService.getEffectiveCourseCountCount(userId))
                    .eq(TUser::getId, userId)
                    .update();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新用户有效课程数量失败:" + e.getMessage());
            throw new RuntimeException("更新用户有效课程数量失败");
        }
    }

    public TUser loginUpdateUserInfo(TUser tUser) {
        try {
            tUser.setLoginAt(LocalDateTime.now());
            //计算有效课程数量(包括未核销和已核销的课程,除去已过期的信息) 临时做法
            tUser.setEffectiveCourseCount((int) courseOrderSplitService.getEffectiveCourseCountCount(tUser.getId()));
            if (tUserService.updateById(tUser)) {
                return getUserCardInfo(tUser);
            }
            return tUser;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新用户信息失败:" + e.getMessage());
            throw new RuntimeException("更新用户信息失败");
        }

    }


    /**
     * 更新用户已完成的课程数量(包括已预约课程和已预约未签到的课程)
     *
     * @param userId
     * @return
     */
    public boolean updateCompleteCourseNumber(Integer userId) {
        try {

            return tUserService.lambdaUpdate().set(TUser::getCompletedCourseCount, (int) courseOrderSplitService.getCompleteCourseNumber(userId))
                    .eq(TUser::getId, userId)
                    .update();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
