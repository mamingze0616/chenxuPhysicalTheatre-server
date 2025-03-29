package com.chenxu.physical.theatre.database.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;

import java.util.List;

/**
 * @author mamingze
 * @description 针对表【T_APPOINTMENT_INFO(课程预约表)】的数据库操作Service
 * @createDate 2025-03-29 22:36:52
 */
public interface TAppointmentInfoService extends IService<TAppointmentInfo> {
    List<TAppointmentInfo> getAppointmentInfoByCourseId(Integer courseId);

    List<TAppointmentInfo> getAppointmentInfosByUserId(Integer userId);

}
