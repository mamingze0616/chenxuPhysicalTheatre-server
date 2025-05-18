package com.chenxu.physical.theatre.bussiness.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.database.constant.TCourseOrderSpiltStatusEnum;
import com.chenxu.physical.theatre.database.domain.TCourseOrder;
import com.chenxu.physical.theatre.database.domain.TCourseOrderSpilt;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.service.TCourseOrderService;
import com.chenxu.physical.theatre.database.service.TCourseOrderSpiltService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author mamingze
 * @version 1.0
 * @title CourseOrderSplitService
 * @description
 * @create 2025/5/19 00:17
 */
@Service
public class CourseOrderSplitService {
    private static final Logger logger = LoggerFactory.getLogger(CourseOrderSplitService.class);

    @Autowired
    TCourseOrderService tCourseOrderService;
    @Autowired
    TCourseOrderSpiltService tCourseOrderSpiltService;

    @Autowired
    UserService userService;

    public boolean updateById(TCourseOrderSpilt tCourseOrderSpilt) {

        return tCourseOrderSpiltService.updateById(tCourseOrderSpilt);
    }

    public void splitCourseOrder(TCourseOrder tCourseOrder) {
        logger.info("splitCourseOrder::tCourseOrder = [{}]", tCourseOrder);
        //支付类
        try {
            List<TCourseOrderSpilt> spilts = new ArrayList<>();
            //先判断查分表里面是否拆分数据
            spilts = tCourseOrderSpiltService.list(new QueryWrapper<TCourseOrderSpilt>().eq("course_order_id", tCourseOrder.getId()));
            if (spilts.size() > 0) {
                logger.info("此订单已拆分");
                return;
            }
            for (int i = 0; i < tCourseOrder.getCourseNumber(); i++) {
                TCourseOrderSpilt tCourseOrderSpilt = new TCourseOrderSpilt();
                tCourseOrderSpilt.setCourseOrderId(tCourseOrder.getId());
                tCourseOrderSpilt.setUserId(tCourseOrder.getUserId());
                tCourseOrderSpilt.setStatus(TCourseOrderSpiltStatusEnum.UNWRITE_OFF);
                tCourseOrderSpilt.setSplitIndex(i);
                LocalDate startTime = tCourseOrder.getStartTime();
                tCourseOrderSpilt.setEndTime(startTime.plusDays(tCourseOrder.getValidityPeriod()));
                spilts.add(tCourseOrderSpilt);
            }
            if (tCourseOrderSpiltService.saveBatch(spilts)) {
                logger.info("拆分成功");
                //查询未核销的拆分课程数量
                userService.updateEffectiveCourseCount(tCourseOrder.getUserId());
            }

        } catch (Exception e) {
            throw e;
        }


    }

    public TCourseOrderSpilt getUnWriteOffCourseOrderSpilt(Integer userId) {
        logger.info("getUnWriteOffCourseOrderSpilt::userId = [{}]", userId);
        //支付类
        try {
            Optional.ofNullable(userId).orElseThrow(() -> new RuntimeException("userId为空"));
            //获取最近日期的未核销的的拆分课程订单
            return tCourseOrderSpiltService.getOne(new QueryWrapper<TCourseOrderSpilt>()
                    .eq("user_id", userId)
                    .eq("status", TCourseOrderSpiltStatusEnum.UNWRITE_OFF).orderByDesc("end_time").last("limit 1"));
        } catch (Exception e) {
            throw e;
        }
    }

    public void splitCourseOrder(TUser user) {
        logger.info("splitCourseOrder::user = [{}]", user);
        //支付类
        try {
            Optional.ofNullable(user.getId()).orElseThrow(() -> new RuntimeException("id为空"));
            //查询出该用户的所有订单
            tCourseOrderService.list(new QueryWrapper<TCourseOrder>().eq("user_id", user.getId()))
                    .forEach(tCourseOrder -> {
                        splitCourseOrder(tCourseOrder);
                    });


        } catch (Exception e) {
            throw e;
        }


    }


}
