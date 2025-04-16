package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxu.physical.theatre.database.constant.TAppointmentInfoTypeEnum;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.mapper.TCourseMapper;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import com.chenxu.physical.theatre.database.service.TCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author mamingze
 * @description 针对表【T_COURSE(课程信息表)】的数据库操作Service实现
 * @createDate 2025-03-27 10:14:18
 */
@Service
public class TCourseServiceImpl extends ServiceImpl<TCourseMapper, TCourse>
        implements TCourseService {
    @Autowired
    private TAppointmentInfoService tAppointmentInfoService;

    @Override
    public PageDTO<TCourse> selectPageTCourseList(PageDTO<TCourse> page, TCourse course) {
        QueryWrapper<TCourse> queryWrapper = new QueryWrapper<>();
        Optional.ofNullable(course.getCourseName()).ifPresent(name -> queryWrapper.like("course_name", name));
        Optional.ofNullable(course.getType()).ifPresent(type -> queryWrapper.eq("type", type));
        Optional.ofNullable(course.getDate()).ifPresent(date -> queryWrapper.ge("date", date));
        PageDTO<TCourse> pageDTO = page(page, queryWrapper);
        //分页结果
        List<TCourse> tCourseList = Optional.ofNullable(pageDTO.getRecords()).orElse(new ArrayList<TCourse>());
        //使用子查询做数据填充
        List<Integer> integerList = tCourseList.stream().map(TCourse::getId).collect(Collectors.toList());
        if (integerList.isEmpty()) {
            return pageDTO;
        }
        Map<Integer, List<TAppointmentInfo>> subAppointmentInfoMap = tAppointmentInfoService.getAppointmentInfoByCourseIds(integerList)
                .stream().collect(Collectors.groupingBy(TAppointmentInfo::getCourseId));
        tCourseList.forEach(tCourse -> {
            if (subAppointmentInfoMap.containsKey(tCourse.getId())) {
                tCourse.setAppointmentInfos(subAppointmentInfoMap.get(tCourse.getId()));
            }
        });
        //数据回填
        pageDTO.setRecords(tCourseList);
        return pageDTO;
    }

    @Override
    public List<TCourse> getBookableCoursesWithAppointmentInfoByUserid(Integer userid) {
        return baseMapper.getBookableCoursesWithAppointmentInfoByUserid(userid);
    }

    @Override
    public List<TCourse> getAleardyBookedCoursersWithAppointmentInfoByUserid(Integer userid) {
        return baseMapper.getAleardyBookedCoursersWithAppointmentInfoByUserid(userid);
    }

    @Override
    public TCourse getCourserWithAppointmentInfoByCourseId(Integer courseId) {
        return baseMapper.getCourserWithAppointmentInfoByCourseId(courseId);
    }


    @Override
    public void setCourseFinished(Integer courseId) {
        this.lambdaUpdate()
                .eq(TCourse::getId, courseId).set(TCourse::getType, TCourseType.FINISHED.getCode()).update();
        tAppointmentInfoService.lambdaUpdate()
                .eq(TAppointmentInfo::getCourseId, courseId)
                .eq(TAppointmentInfo::getType, TAppointmentInfoTypeEnum.SIGNED.getCode())
                .set(TAppointmentInfo::getType, TAppointmentInfoTypeEnum.LEARNED.getCode())
                .update();
        tAppointmentInfoService.lambdaUpdate()
                .eq(TAppointmentInfo::getCourseId, courseId)
                .eq(TAppointmentInfo::getType, TAppointmentInfoTypeEnum.APPOINTED.getCode())
                .set(TAppointmentInfo::getType, TAppointmentInfoTypeEnum.NOT_SIGNED.getCode())
                .update();
    }

    @Override
    public void updateCourseBookedNumber(Integer courseId) {
        long count = tAppointmentInfoService.count(new QueryWrapper<TAppointmentInfo>().eq("course_id", courseId)
                .in("type", TAppointmentInfoTypeEnum.APPOINTED.getCode(), TAppointmentInfoTypeEnum.LEARNED.getCode(), TAppointmentInfoTypeEnum.SIGNED.getCode()));
        this.lambdaUpdate()
                .eq(TCourse::getId, courseId)
                .set(TCourse::getBookedNum, Integer.valueOf((int) count))
                .update();
    }
}




