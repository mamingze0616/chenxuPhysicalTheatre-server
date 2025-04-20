package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxu.physical.theatre.database.constant.TCourseOrderStatus;
import com.chenxu.physical.theatre.database.domain.TCourseOrder;
import com.chenxu.physical.theatre.database.mapper.TCourseOrderMapper;
import com.chenxu.physical.theatre.database.service.TCourseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mamingze
 * @description 针对表【T_COURSE_ORDER(课程订单信息)】的数据库操作Service实现
 * @createDate 2025-03-28 17:19:58
 */
@Service
public class TCourseOrderServiceImpl extends ServiceImpl<TCourseOrderMapper, TCourseOrder> implements TCourseOrderService {
    private static final Logger logger = LoggerFactory.getLogger(TCourseOrderServiceImpl.class);

    @Override
    public int getTotalCourseNumberByUserId(Integer userId) {
        AtomicInteger totalCourseNumber = new AtomicInteger();
        totalCourseNumber.set(0);
        try {
            //查询当前用户购买的课时
            List<TCourseOrder> tCourseOrderList = list(new QueryWrapper<TCourseOrder>()
                    .eq("user_id", userId).eq("status", TCourseOrderStatus.NORMAL.getCode()));
            if (tCourseOrderList == null || tCourseOrderList.size() == 0) {
                throw new RuntimeException("该用户没有可用课时订单");
            }
            //有订单消息
            tCourseOrderList.forEach(tCourseOrder -> {
                //原子加和
                totalCourseNumber.addAndGet(tCourseOrder.getCourseNumber());
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return totalCourseNumber.get();
        }
    }
}




