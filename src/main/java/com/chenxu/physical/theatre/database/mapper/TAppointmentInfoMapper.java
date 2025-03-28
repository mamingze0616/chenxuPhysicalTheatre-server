package com.chenxu.physical.theatre.database.mapper;

import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mamingze
* @description 针对表【T_APPOINTMENT_INFO(课程预约表)】的数据库操作Mapper
* @createDate 2025-03-27 15:12:23
* @Entity com.chenxu.physical.theatre.database.domain.TAppointmentInfo
*/
public interface TAppointmentInfoMapper extends BaseMapper<TAppointmentInfo> {
    List<TAppointmentInfo> getAppointmentInfoByCourseId(Integer courseId);

}




