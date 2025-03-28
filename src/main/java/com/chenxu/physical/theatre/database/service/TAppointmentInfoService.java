package com.chenxu.physical.theatre.database.service;

import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author mamingze
* @description 针对表【T_APPOINTMENT_INFO(课程预约表)】的数据库操作Service
* @createDate 2025-03-27 15:12:23
*/
public interface TAppointmentInfoService extends IService<TAppointmentInfo> {
    List<TAppointmentInfo> getAppointmentInfoByCourseId(Integer courseId);

}
