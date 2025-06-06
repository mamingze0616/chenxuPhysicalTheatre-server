package com.chenxu.physical.theatre.bussiness.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.database.constant.TAppointmentInfoTypeEnum;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import com.chenxu.physical.theatre.database.service.TCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author mamingze 预约课程服务
 * @version 1.0
 * @title BookedCourseService
 * @description
 * @create 2025/5/19 01:44
 */
@Service
public class BookedCourseService {
    private static final Logger logger = LoggerFactory.getLogger(BookedCourseService.class);
    @Autowired
    TCourseService courseService;
    @Autowired
    TAppointmentInfoService appointmentInfoService;
    @Autowired
    CourseOrderSplitService courseOrderSplitService;
    @Autowired
    UserService userService;
    @Autowired
    SubscribeMessageService subscribeMessageService;

    //预约某个课程
    public boolean doBookedCourse(TAppointmentInfo appointmentInfo) {
        try {
            TUser currentUser = userService.getById(appointmentInfo.getUserId());
            logger.info("当前用户的总有效课程数量为：{}", currentUser.getEffectiveCourseCount());
            logger.info("当前用户的总以上课程数量为：{}", currentUser.getCompletedCourseCount());
            int remaining = currentUser.getEffectiveCourseCount() - currentUser.getCompletedCourseCount();
            if (remaining <= 0) {
                throw new RuntimeException("课时用尽无法课程");
            }

            TCourse tCourse = Optional.ofNullable(courseService.getById(appointmentInfo.getCourseId()))
                    .orElseThrow(() -> new RuntimeException("课程不存在"));
            //课程状态判断
            if (tCourse.getType().equals(TCourseType.START_SIGNING_IN.getCode())) {
                throw new RuntimeException("课程已开课,无法预约");
            } else if (tCourse.getType().equals(TCourseType.FINISHED.getCode())) {
                throw new RuntimeException("课程已结束,无法预约");
            } else if (tCourse.getType().equals(TCourseType.DELETED.getCode())) {
                throw new RuntimeException("课程已取消,无法预约");
            }
            logger.info("当前课程的预约人数为：{}", tCourse.getBookedNum());
            logger.info("当前课程的最大预约人数为：{}", tCourse.getMaximum());
            if (tCourse.getBookedNum() >= tCourse.getMaximum()) {
                throw new RuntimeException("课程预约人数已满,无法预约");
            }
            //先查询是否已经预约过,有取消预约状态的会把状态改为已预约,
            //可预约默认还没开课,所以不会是已学,只可能是已预约或者取消预约
            //已预约的情况的信息先删除,在增加预约信息(保证了预约信息的唯一性)
            TAppointmentInfo hasAppointmentInfo = appointmentInfoService.getOne(new QueryWrapper<TAppointmentInfo>()
                    .eq("course_id", appointmentInfo.getCourseId())
                    .eq("user_id", appointmentInfo.getUserId()));
            if (hasAppointmentInfo != null && hasAppointmentInfo.getType() == TAppointmentInfoTypeEnum.APPOINTED) {
                throw new RuntimeException("已预约该课程");
            } else {
                //把之前预约信息删除
                appointmentInfoService.removeById(hasAppointmentInfo);
            }
            //新增预约信息
            appointmentInfo.setType(TAppointmentInfoTypeEnum.APPOINTED);
            appointmentInfo.setCreateAt(LocalDateTime.now());
            if (appointmentInfoService.save(appointmentInfo)) {

                //核销
                courseOrderSplitService.writeOffCourseOrderSpilt(appointmentInfo);
                //更新用户已学课程数量
                userService.updateCompleteCourseNumber(appointmentInfo.getUserId());
                //更新客户ke课程数量
                courseService.updateCourseBookedNumber(appointmentInfo.getCourseId());

                subscribeMessageService.sendBookedSuccessMessage(currentUser.getOpenid(),
                        tCourse.getCourseName(),
                        tCourse.getStartTime(),
                        appointmentInfo.getCreateAt());

            } else {
                throw new RuntimeException("预约写入失败");
            }
            return true;
        } catch (Exception e) {
            throw e;
        }

    }

    public boolean cancelCourseAppointment(TAppointmentInfo appointmentInfo) {
        try {
            TAppointmentInfo currentAppointmentInfo = Optional.ofNullable(appointmentInfoService.getOne(new QueryWrapper<TAppointmentInfo>()
                            .eq("course_id", appointmentInfo.getCourseId()).eq("user_id", appointmentInfo.getUserId())))
                    .orElseThrow(() -> new RuntimeException("没有该预约信息"));
            TCourse course = Optional.ofNullable(courseService.getById(appointmentInfo.getCourseId())).orElseThrow(() -> new RuntimeException("课程不存在"));
            if (LocalDateTime.now().isAfter(course.getStartTime().minusHours(24))) {
                //如果不满24小时,则不能取消预约
                throw new RuntimeException("距离开始不满24小时,不能取消预约");
            } else {
                //如果大于24小时,则取消预约
                currentAppointmentInfo.setType(TAppointmentInfoTypeEnum.CANCELED);
                appointmentInfoService.updateById(currentAppointmentInfo);
                courseOrderSplitService.setUnWriteOffByAppointmentInfoId(currentAppointmentInfo);
                //更新客户已学课程数量
                userService.updateCompleteCourseNumber(appointmentInfo.getUserId());
                TUser currentUser = userService.getById(appointmentInfo.getUserId());
                subscribeMessageService.sendBookedCancelMessage(currentUser.getOpenid(),
                        course.getCourseName(),
                        course.getStartTime(),
                        "用户自行取消预约", "用户自行取消预约");
                userService.getAdminAndSuperAdmin().forEach(admin -> {
                    subscribeMessageService.sendBookedCancelMessage(admin.getOpenid(),
                            course.getCourseName(),
                            course.getStartTime(),
                            "用户" + currentUser.getNickname() + "自行取消预约", "用户自行取消预约");
                });
                return courseService.updateCourseBookedNumber(appointmentInfo.getCourseId());
            }
        } catch (Exception e) {
            throw e;
        }


    }

}
