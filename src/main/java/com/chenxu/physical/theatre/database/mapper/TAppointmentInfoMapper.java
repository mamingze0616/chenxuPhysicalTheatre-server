package com.chenxu.physical.theatre.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;

import java.time.LocalDate;
import java.util.List;

/**
 * @author mamingze
 * @description 针对表【T_APPOINTMENT_INFO(课程预约表)】的数据库操作Mapper
 * @createDate 2025-03-29 22:36:52
 * @Entity com.chenxu.physical.theatre.database.domain.TAppointmentInfo
 */
public interface TAppointmentInfoMapper extends BaseMapper<TAppointmentInfo> {
    List<TAppointmentInfo> getAppointmentInfoByCourseId(Integer courseId);

    List<TAppointmentInfo> getAppointmentInfoByCourseIds(List<Integer> courseIds);

    List<TAppointmentInfo> getAppointmentInfosByUserIdAndDate(Integer userId, LocalDate date);

    List<TAppointmentInfo> getAllAppointmentInfosByUserId(Integer userId);

    List<TAppointmentInfo> getAllAppointmentInfosByUserIds(List<Integer> userIds);

    Page<TAppointmentInfo> getAppointmentInfoList(Page<TAppointmentInfo> page);

}




