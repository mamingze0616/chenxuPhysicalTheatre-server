package com.chenxu.physical.theatre.bussiness.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.service.TCourseService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author mamingze
 * @version 1.0
 * @title CourseSetFinishedScheduled
 * @description
 * @create 2025/5/24 17:43
 */
@Component
public class CourseSetFinishedScheduled {
    private static final Logger logger = LoggerFactory.getLogger(CourseSetFinishedScheduled.class);
    @Autowired
    private TCourseService courseService;

    /**
     * 定时任务，每小时的第三分钟执行一次
     * 对于已经签到的课程,过了课程结束时间的话进行结束
     */
    @Scheduled(cron = "0 0/15 * * * *")
    @SchedulerLock(name = "checkCourseTypeTask")
    public void checkeCourseTypeTask() {
        logger.info("定时任务开始执行,检查课程状态");
        List<TCourse> courseList = courseService.list(new QueryWrapper<TCourse>()
                .eq("type", TCourseType.START_SIGNING_IN.getCode())
                .lt("END_TIME", LocalDate.now()));
        logger.info("检查到{}个课程已过结束时间", courseList.size());
        //对于已上的课程,
        courseList.stream().forEach(item -> {
            logger.info("课程{}已结束,设置课程状态为已结束", item.getCourseName());
            courseService.setCourseFinished(item.getId());
            //暂时不更新拆分表的数据
        });

    }

    /**
     * 定时任务,每日0点零一分钟执行
     * 检测该天的课程的预约人数,如果预约人数不满,则课程取消
     */
    @Scheduled(cron = "0 1 0 * * *")
    @SchedulerLock(name = "checkCourseBookedNumTask")
    public void updateCourseBookedNumber() {
        logger.info("定时任务开始执行,检查今天的所有课程的预约人数");
        List<TCourse> courseList = courseService.list(new QueryWrapper<TCourse>()
                .eq("type", TCourseType.NOT_START.getCode())
                .le("date", LocalDate.now()));
        logger.info("今天有{}个课程", courseList.size());
        courseList.forEach(course -> {
            logger.info("该课程的名称:{},该课程的预约人数:{},该课程的最小预约人数:{}", course.getCourseName(),
                    course.getBookedNum(), course.getMinimum());
            if (course.getBookedNum() < course.getMinimum()) {
                logger.info("该课程{}的预约人数小于最小预约人数,课程取消", course.getCourseName());
                courseService.setCourseCanceled(course.getId());
            }

        });
    }

    /**
     * 定时任务十五分钟执行一次,检查课程是否可以开启签到
     */
    @Scheduled(cron = "0 0/15 * * * ?")
    @SchedulerLock(name = "checkCourseSignInTask")
    public void checkCourseSignInTask() {
        List<TCourse> courseList = courseService.list(new QueryWrapper<TCourse>()
                .eq("type", TCourseType.NOT_START.getCode())
                .le("start_time", LocalDateTime.now())
                .eq("date", LocalDate.now()));
        courseList.forEach(course -> {
            try {
                courseService.setStartSigningIn(course.getId());
            } catch (Exception e) {
                logger.error("设置课程{}为开始签到失败", course.getCourseName());
            }

        });
    }
}
