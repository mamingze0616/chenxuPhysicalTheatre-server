package com.chenxu.physical.theatre.database.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenxu.physical.theatre.database.domain.TCourse;

import java.util.List;

/**
 * @author mamingze
 * @description 针对表【T_COURSE(课程信息表)】的数据库操作Service
 * @createDate 2025-03-27 10:14:18
 */
public interface TCourseService extends IService<TCourse> {
    PageDTO<TCourse> selectPageTCourseList(PageDTO<TCourse> page, TCourse course);

    //获取某user的所有可预约课程,携带该课程的预约信息
    List<TCourse> getBookableCoursesWithAppointmentInfoByUserid(Integer userid);

    List<TCourse> getAleardyBookedCoursersWithAppointmentInfoByUserid(Integer userid);

    //将往期课程的type设置为已上,将签到的设置为已学,将已预约的设置为未签到
    public void setCourseFinished(Integer courseId);

    public void updateCourseBookedNumber(Integer courseId);
}
