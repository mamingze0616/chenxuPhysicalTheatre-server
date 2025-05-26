package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxu.physical.theatre.bussiness.service.CourseOrderSplitService;
import com.chenxu.physical.theatre.bussiness.service.SubscribeMessageService;
import com.chenxu.physical.theatre.bussiness.service.UserService;
import com.chenxu.physical.theatre.database.constant.TAppointmentInfoTypeEnum;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.mapper.TCourseMapper;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import com.chenxu.physical.theatre.database.service.TCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
public class TCourseServiceImpl extends ServiceImpl<TCourseMapper, TCourse> implements TCourseService {
    @Autowired
    private TAppointmentInfoService tAppointmentInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    CourseOrderSplitService courseOrderSplitService;

    @Autowired
    SubscribeMessageService subscribeMessageService;

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
        Map<Integer, List<TAppointmentInfo>> subAppointmentInfoMap = tAppointmentInfoService.getAppointmentInfoByCourseIds(integerList).stream().collect(Collectors.groupingBy(TAppointmentInfo::getCourseId));
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
    public List<TCourse> getBookableCoursesWithAppointmentInfoByUserid(Integer userid, LocalDate startDate, LocalDate endDate) {
        return baseMapper.getBookableCoursesWithAppointmentInfoByUserid(userid, startDate, endDate);
    }

    @Override
    public List<TCourse> getAleardyBookedCoursersWithAppointmentInfoByUserid(Integer userid, LocalDate startDate, LocalDate endDate) {
        return baseMapper.getAleardyBookedCoursersWithAppointmentInfoByUserid(userid, startDate, endDate);
    }

    @Override
    public TCourse getCourserWithAppointmentInfoByCourseId(Integer courseId) {
        return baseMapper.getCourserWithAppointmentInfoByCourseId(courseId);
    }


    @Override
    public void setCourseFinished(Integer courseId) {
        this.lambdaUpdate().eq(TCourse::getId, courseId).set(TCourse::getType, TCourseType.FINISHED.getCode()).update();
        //更新已签到的人为已学
        tAppointmentInfoService.lambdaUpdate().eq(TAppointmentInfo::getCourseId, courseId).eq(TAppointmentInfo::getType, TAppointmentInfoTypeEnum.SIGNED.getCode()).set(TAppointmentInfo::getType, TAppointmentInfoTypeEnum.LEARNED.getCode()).update();
        //更新未签到的人为已预约未签到
        tAppointmentInfoService.lambdaUpdate().eq(TAppointmentInfo::getCourseId, courseId).eq(TAppointmentInfo::getType, TAppointmentInfoTypeEnum.APPOINTED.getCode()).set(TAppointmentInfo::getType, TAppointmentInfoTypeEnum.NOT_SIGNED.getCode()).update();
    }

    @Override
    public boolean updateCourseBookedNumber(Integer courseId) {
        long count = tAppointmentInfoService.count(new QueryWrapper<TAppointmentInfo>().eq("course_id", courseId).in("type", TAppointmentInfoTypeEnum.APPOINTED.getCode(), TAppointmentInfoTypeEnum.LEARNED.getCode(), TAppointmentInfoTypeEnum.SIGNED.getCode()));
        return this.lambdaUpdate().eq(TCourse::getId, courseId).set(TCourse::getBookedNum, Integer.valueOf((int) count)).update();
    }

    @Override
    public void setCourseCanceled(Integer courseId) {

        lambdaUpdate().eq(TCourse::getId, courseId).set(TCourse::getType, TCourseType.CANCELED.getCode()).update();

        //更新已预约的人设置为取消预约
        tAppointmentInfoService.lambdaUpdate().eq(TAppointmentInfo::getCourseId, courseId)
                .eq(TAppointmentInfo::getType, TAppointmentInfoTypeEnum.APPOINTED.getCode())
                .set(TAppointmentInfo::getType, TAppointmentInfoTypeEnum.CANCELED.getCode())
                .update();
        TCourse tCourse = getCourserWithAppointmentInfoByCourseId(courseId);
        tCourse.getAppointmentInfos().stream().forEach(appointmentInfo -> {
            TUser tUser = userService.getById(appointmentInfo.getUserId());
            subscribeMessageService.sendBookedCancelMessage(tUser.getOpenid(), tCourse.getCourseName(),
                    tCourse.getStartTime(),
                    "开课前预约人员不足预约取消", "该课程未满足最低开课人数,无法开课");
            // 取消预约更新拆分表数据
            courseOrderSplitService.setUnWriteOffByAppointmentInfoId(appointmentInfo);
            //更新客户已学课程数量
            userService.updateCompleteCourseNumber(appointmentInfo.getUserId());

        });
        updateCourseBookedNumber(courseId);
    }

    @Override
    public void setStartSigningIn(Integer courseId) {
        TCourse tCourse = Optional.ofNullable(getById(courseId)).orElseThrow(() -> new RuntimeException("课程不存在"));
        if (tCourse.getBookedNum() < tCourse.getMinimum()) {
            throw new RuntimeException("人数不足,无法开始签到");
        }

        if (!lambdaUpdate().set(TCourse::getType, TCourseType.START_SIGNING_IN.getCode())
                .eq(TCourse::getId, tCourse.getId())
                .update()) {
            throw new RuntimeException("更新失败");
        }
    }
}




