package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.mapper.TAppointmentInfoMapper;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author mamingze
 * @description 针对表【T_APPOINTMENT_INFO(课程预约表)】的数据库操作Service实现
 * @createDate 2025-03-29 22:36:52
 */
@Service
public class TAppointmentInfoServiceImpl extends ServiceImpl<TAppointmentInfoMapper, TAppointmentInfo>
        implements TAppointmentInfoService {

    @Override
    public List<TAppointmentInfo> getAppointmentInfoByCourseId(Integer courseId) {
        return baseMapper.getAppointmentInfoByCourseId(courseId);
    }

    @Override
    public List<TAppointmentInfo> getAppointmentInfosByUserId(Integer userId) {
        return baseMapper.getAppointmentInfosByUserId(userId);
    }

    @Override
    public List<TAppointmentInfo> getAppointmentInfosByUserIdAndDate(Integer userId, LocalDate date) {
        return baseMapper.getAppointmentInfosByUserIdAndDate(userId, date);
    }
}




