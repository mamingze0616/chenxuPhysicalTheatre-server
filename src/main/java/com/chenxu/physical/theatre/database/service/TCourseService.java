package com.chenxu.physical.theatre.database.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenxu.physical.theatre.database.domain.TCourse;

import java.time.LocalDate;
import java.util.List;

/**
 * @author mamingze
 * @description 针对表【T_COURSE(课程信息表)】的数据库操作Service
 * @createDate 2025-03-27 10:14:18
 */
public interface TCourseService extends IService<TCourse> {
    PageDTO<TCourse> selectPageTCourseList(PageDTO<TCourse> page, TCourse course);

    //获取某user的所有可预约课程,携带该课程的预约信息
    List<TCourse> getBookableCoursesWithAppointmentInfoByUserid(Integer userid, LocalDate startDate, LocalDate endDate);

    List<TCourse> getAleardyBookedCoursersWithAppointmentInfoByUserid(Integer userid, LocalDate startDate, LocalDate endDate);

    TCourse getCourserWithAppointmentInfoByCourseId(Integer courseId);

    /**
     * 将往期课程的type设置为已上,将签到的设置为已学,将已预约的设置为未签到
     *
     * @param courseId
     */
    void setCourseFinished(Integer courseId);

    boolean updateCourseBookedNumber(Integer courseId);

    /**
     * 将课程设置为已取消(主要为未上的课程,从定时任务而来的
     *
     * @param courseId
     */
    void setCourseCanceled(Integer courseId);

    void setStartSigningIn(Integer courseId);
}
