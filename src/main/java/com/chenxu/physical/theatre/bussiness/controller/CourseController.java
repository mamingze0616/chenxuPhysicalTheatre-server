package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiDateRequest;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.dto.ApiWeekCourseModel;
import com.chenxu.physical.theatre.database.constant.ChineseDayOfWeek;
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
        ApiResponse apiResponse = new ApiResponse();
        //先按照日期查询课程
        try {
            //先查询两个星期
            List<TCourse> list = courseService.list(new QueryWrapper<TCourse>()
                    .ge("date", date.getDate())
                    .lt("date", date.getDate().plusDays(14)));
            //按照日期排序
            list.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
            List<ApiWeekCourseModel> apiWeekCourseModels = new ArrayList<>();
            for (int i = 0; i < 14; i++) {
                LocalDate plusDaysdate = date.getDate().plusDays(i);
                ApiWeekCourseModel apiWeekCourseModel = new ApiWeekCourseModel();
                apiWeekCourseModel.setDate(plusDaysdate.toString());
                apiWeekCourseModel.setWeekday(ChineseDayOfWeek.of(plusDaysdate.getDayOfWeek().getValue()).getDesc());
                apiWeekCourseModel.setList(list.stream().filter(c -> c.getDate().equals(plusDaysdate)).toList());
                apiWeekCourseModels.add(apiWeekCourseModel);
            }
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(apiWeekCourseModels);
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }

        return apiResponse;
    }

}
