package com.chenxu.physical.theatre.bussiness.schedule;

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
 * @title UserCardTypeScheduled
 * @description
 * @create 2025/6/8 18:10
 */
@Component
public class UserCardTypeScheduled {
    private static final Logger logger = LoggerFactory.getLogger(CourseSetFinishedScheduled.class);
    @Autowired
    UserService userService;

    /**
     * 每日0点更新用户会员等级,查看是否过期
     */
    @Scheduled(cron = "5 0 0 * * ?")
    @SchedulerLock(name = "updateUserCardType")
    public void updateUserCardType() {
        logger.info("updateUserCardType start");
        userService.getAllUser().forEach(item -> {
            logger.info("用户{}开始执行定时任务：检查会员{}是否过期", item.getId(), item.getCardType());
//            userService.updateUserCardType(item);
        });
        logger.info("updateUserCardType end");
    }
}
