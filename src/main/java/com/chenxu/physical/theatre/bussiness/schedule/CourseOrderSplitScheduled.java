package com.chenxu.physical.theatre.bussiness.schedule;

import com.chenxu.physical.theatre.bussiness.service.CourseOrderSplitService;
import com.chenxu.physical.theatre.bussiness.service.UserService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author mamingze
 * @version 1.0
 * @title CourseOrderSplitScheduled
 * @description 拆分订单类相关定时任务
 * @create 2025/6/5 13:41
 */
@Component
public class CourseOrderSplitScheduled {
    private static final Logger logger = LoggerFactory.getLogger(CourseOrderSplitScheduled.class);
    @Autowired
    CourseOrderSplitService courseOrderSplitService;
    @Autowired
    UserService userService;

    /**
     * 定时任务：更新是否核销课程已经过期
     */
    @Scheduled(cron = "3 0 0 * * *")
    @SchedulerLock(name = "checkSplitCourseOrderStatusTask")
    public void checkSplitCourseOrderStatusTask() {
        logger.info("开始执行定时任务：检查代核销课程是否已经过期");
        courseOrderSplitService.updateAllUnWriteOffCourseOrderSpiltStatus();
        userService.getAllUser().forEach(item -> {
            logger.info("用户id{}开始更新有效课程数量", item.getId());
            userService.updateEffectiveCourseCount(item.getId());
        });
    }

}
