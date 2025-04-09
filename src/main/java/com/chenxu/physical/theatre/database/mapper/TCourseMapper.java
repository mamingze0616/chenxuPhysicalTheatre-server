package com.chenxu.physical.theatre.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenxu.physical.theatre.database.domain.TCourse;

import java.util.List;

/**
 * @author mamingze
 * @description 针对表【T_COURSE(课程信息表)】的数据库操作Mapper
 * @createDate 2025-03-27 10:14:18
 * @Entity com.chenxu.physical.theatre.database.domain.TCourse
 */
public interface TCourseMapper extends BaseMapper<TCourse> {

    List<TCourse> getBookableCoursesWithAppointmentInfoByUserid(Integer userid);

    List<TCourse> getAleardyBookedCoursersWithAppointmentInfoByUserid(Integer userid);

}




