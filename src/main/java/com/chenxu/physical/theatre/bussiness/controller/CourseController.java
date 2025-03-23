package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiDateRequest;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.dto.ApiWeekCourseModel;
import com.chenxu.physical.theatre.database.constant.ChineseDayOfWeek;
import com.chenxu.physical.theatre.database.constant.TCourseConstans;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.constant.TUserType;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.service.TCourseService;
import com.chenxu.physical.theatre.database.service.TUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author mamingze
 * @version 1.0
 * @title CourseController
 * @description
 * @create 2025/3/20 14:00
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    @Autowired
    private TCourseService courseService;
    @Autowired
    private TUserService tUserService;


    @PostMapping("/add")
    public ApiResponse addCourse(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none") String openid, TCourse course) {
        logger.info("addCourse::openid = [{}], course = [{}]", openid, course);
        ApiResponse apiResponse = new ApiResponse();
        //先查询此用户是否有权限操作
        try {
            Optional<TUser> tUserOptions = Optional.ofNullable(tUserService
                    .getOne(new QueryWrapper<TUser>().eq("openid", openid), false));
            tUserOptions.ifPresentOrElse(tUser -> {
                if (TUserType.ADMIN.getCode().compareTo(tUser.getType()) == 0) {
                    //是管理员
                    course.setType(TCourseType.NOT_START.getCode());
                    courseService.save(course);
                } else {
                    //没有权限
                    apiResponse.setCode(Constant.APIRESPONSE_FAIL);
                    apiResponse.setErrorMsg("用户无权限操作");
                }
            }, () -> {
                apiResponse.setCode(Constant.APIRESPONSE_FAIL);
                apiResponse.setErrorMsg("用户不存在");
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }

        return apiResponse;
    }

    /**
     * 获取从date日期开始的两周内的课程信息
     *
     * @param openid
     * @param date
     * @return
     */

    @PostMapping("/getTwoWeekCourses")
    public ApiResponse getTwoWeekCourses(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none") String openid,
                                         @RequestBody ApiDateRequest date) {
        logger.info("getTwoWeekCourses::openid = [{}], date = [{}]", openid, date);
        ApiResponse apiResponse = new ApiResponse();
        //先按照日期查询课程
        try {
            //没有日期的话默认当前日期
            LocalDate tempDate = Optional.ofNullable(date.getDate()).orElse(LocalDate.now());
            //先查询两个星期
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(checkTwoWeekCourses(tempDate));
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }

        return apiResponse;
    }

    private List<ApiWeekCourseModel> checkTwoWeekCourses(LocalDate date) {
        List<ApiWeekCourseModel> apiWeekCourseModels = new ArrayList<>(14);
        List<TCourse> list = courseService.list(new QueryWrapper<TCourse>()
                //大于等于date
                .ge("date", date)
                //小于两星期后的日期
                .lt("date", date.plusDays(TCourseConstans.TWO_WEEK_DAYS)));
        for (int i = 0; i < TCourseConstans.TWO_WEEK_DAYS; i++) {
            LocalDate plusDaysdate = date.plusDays(i);

            ApiWeekCourseModel apiWeekCourseModel = new ApiWeekCourseModel();
            apiWeekCourseModel.setDate(plusDaysdate.toString());
            apiWeekCourseModel.setWeekday(ChineseDayOfWeek.of(plusDaysdate.getDayOfWeek().getValue()).getDesc());
            //当天临时课程
            List<TCourse> tempList = list.stream().filter(c -> c.getDate().equals(plusDaysdate))
                    .collect(Collectors.toList());
            if (tempList.size() == 0) {
                tempList = initEverdayCourse(plusDaysdate);
            }else{
                //暂时不处理其他情况
            }
            tempList.sort((o1, o2) -> o2.getLesson().compareTo(o1.getLesson()));
            apiWeekCourseModel.setList(tempList);
            apiWeekCourseModels.add(apiWeekCourseModel);
        }
        return apiWeekCourseModels;

    }

    private List<TCourse> initEverdayCourse(LocalDate date) {
        List<TCourse> list = new ArrayList<>(TCourseConstans.LESSON_NUMBER);
        for (int i = 1; i <= TCourseConstans.LESSON_NUMBER; i++) {
            StringBuffer courseName = new StringBuffer(date.toString());
            courseName.append("第").append(i).append("节");
            TCourse course = new TCourse();
            course.setCourseName(courseName.toString());
            course.setDate(date);
            course.setLesson(i);
            course.setMaximum(TCourseConstans.MAXIMUM);
            course.setType(TCourseType.NOT_REGISTER.getCode());
            courseService.save(course);
            list.add(course);
        }
        return list;

    }

}
