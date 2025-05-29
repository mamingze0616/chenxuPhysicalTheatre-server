package com.chenxu.physical.theatre.bussiness.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.database.constant.TCourseOrderSpiltStatusEnum;
import com.chenxu.physical.theatre.database.constant.TCourseOrderStatus;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TCourseOrder;
import com.chenxu.physical.theatre.database.domain.TCourseOrderSpilt;
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


    public boolean updateById(TCourseOrderSpilt tCourseOrderSpilt) {

        return tCourseOrderSpiltService.updateById(tCourseOrderSpilt);
    }

    public boolean setUnWriteOffByAppointmentInfoId(TAppointmentInfo appointmentInfo) {
        TCourseOrderSpilt tCourseOrderSpilt = tCourseOrderSpiltService.getOne(new QueryWrapper<TCourseOrderSpilt>()
                .eq("appointment_id", appointmentInfo.getId()));
        tCourseOrderSpilt.setStatus(TCourseOrderSpiltStatusEnum.UNWRITE_OFF);
        tCourseOrderSpilt.setWriteOffDate(LocalDate.now());
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
                tCourseOrderSpilt.setStartTime(startTime);
                tCourseOrderSpilt.setEndTime(startTime.plusDays(tCourseOrder.getValidityPeriod()));
                spilts.add(tCourseOrderSpilt);
            }
            tCourseOrderSpiltService.saveBatch(spilts);
            logger.info("拆分成功");
        } catch (Exception e) {
            throw e;
        }


    }

    /**
     * 获取一条最近的未核销的拆分课程订单,生效日期在今天以及之后的
     *
     * @param userId
     * @return
     */
    public TCourseOrderSpilt getUnWriteOffCourseOrderSpilt(Integer userId) {
        logger.info("getUnWriteOffCourseOrderSpilt::userId = [{}]", userId);
        //支付类
        try {
            Optional.ofNullable(userId).orElseThrow(() -> new RuntimeException("userId为空"));
            updateAllUnWriteOffCourseOrderSpiltStatus();
            //获取最近日期的未核销的的拆分课程订单
            List<TCourseOrderSpilt> canWriteOffCourseOrderSpilt = tCourseOrderSpiltService.list(new QueryWrapper<TCourseOrderSpilt>()
                    .eq("user_id", userId)
                    .eq("status", TCourseOrderSpiltStatusEnum.UNWRITE_OFF)
                    .le("start_time", LocalDate.now())
                    .ge("end_time", LocalDate.now())
                    .orderByAsc("end_time"));
            return canWriteOffCourseOrderSpilt.size() > 0 ? canWriteOffCourseOrderSpilt.get(0) : null;
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 更新所有的代核销的拆分课程订单状态,如果过期则先修改已过期
     *
     * @return
     */
    public boolean updateAllUnWriteOffCourseOrderSpiltStatus() {
        try {
            //获取所有未核销的拆分课程订单
            return tCourseOrderSpiltService.lambdaUpdate()
                    .eq(TCourseOrderSpilt::getStatus, TCourseOrderSpiltStatusEnum.UNWRITE_OFF.getCode())
                    .lt(TCourseOrderSpilt::getEndTime, LocalDate.now())
                    .set(TCourseOrderSpilt::getStatus, TCourseOrderSpiltStatusEnum.EXPIRED.getCode())
                    .update();
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean writeOffCourseOrderSpilt(TAppointmentInfo appointmentInfo) {
        try {
            TCourseOrderSpilt courseOrderSpilt = getUnWriteOffCourseOrderSpilt(appointmentInfo.getUserId());
            courseOrderSpilt.setStatus(TCourseOrderSpiltStatusEnum.WRITE_OFF);
            courseOrderSpilt.setAppointmentId(appointmentInfo.getId());
            courseOrderSpilt.setWriteOffDate(LocalDate.now());
            return updateById(courseOrderSpilt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //支付成功之后该订单所关联的课程订单回调,设置为已支付
    public TCourseOrder payCallback(Integer courseOrderId) {
        try {
            //更新该状态的课程订单为已支付
            //将该订单拆分为拆分表中数据
            TCourseOrder courseOrder = tCourseOrderService.getById(courseOrderId);
            courseOrder.setStatus(TCourseOrderStatus.SUCCESS);
            if (tCourseOrderService.updateById(courseOrder)) {
                splitCourseOrder(courseOrder);
            }
            //更新用户有效课程数量
            return courseOrder;
        } catch (Exception e) {
            throw e;
        }
    }

    //获取某用户的有效课程数量(包括已核销和未核销)
    public long getEffectiveCourseCountCount(Integer userId) {
        return tCourseOrderSpiltService.list(new QueryWrapper<TCourseOrderSpilt>().eq("user_id", userId)
                .in("status", TCourseOrderSpiltStatusEnum.UNWRITE_OFF.getCode()
                        , TCourseOrderSpiltStatusEnum.WRITE_OFF.getCode())).stream().count();
    }

    //获取某用户的已上的课程数量(包括已核销的课程)
    public long getCompleteCourseNumber(Integer userId) {
        return tCourseOrderSpiltService.list(new QueryWrapper<TCourseOrderSpilt>().eq("user_id", userId)
                .eq("status", TCourseOrderSpiltStatusEnum.WRITE_OFF.getCode())).stream().count();
    }
}
