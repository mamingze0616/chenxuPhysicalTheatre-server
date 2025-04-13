package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.dto.ApiWeekCourseModel;
import com.chenxu.physical.theatre.database.constant.ChineseDayOfWeek;
import com.chenxu.physical.theatre.database.constant.TCourseStartTime;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import com.chenxu.physical.theatre.database.service.TCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private TAppointmentInfoService appointmentInfoService;


    @PostMapping("/addCourse")
    public ApiResponse addCourse(@RequestBody TCourse course) {
        logger.info("addCourse::course = [{}]", course);
        ApiResponse apiResponse = new ApiResponse();
        //先查询此用户是否有权限操作
        try {
            //先查询该节课是否有课程
            if (courseService.getOne(new QueryWrapper<TCourse>()
                    .eq("date", course.getDate())
                    .eq("lesson", course.getLesson())) != null) {
                throw new RuntimeException("该课程已存在");
            }
            course.setType(TCourseType.NOT_START);
            if (course.getStartTime() == null) {
                LocalDateTime startTime = course.getDate().atTime(TCourseStartTime.getStartTimeByCode(course.getLesson()));
                course.setStartTime(startTime);
                course.setEndTime(startTime.plusHours(1));
            }
            courseService.save(course);
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(course);
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    /**
     * 管理员获取课程信息
     *
     * @param apiWeekCourseModel
     * @return
     */
    @PostMapping("/adminGetCourseList")
    public ApiResponse adminGetCourseList(@RequestBody ApiWeekCourseModel apiWeekCourseModel) {
        logger.info("adminGetCourseList::date = [{}]", apiWeekCourseModel);
        ApiResponse apiResponse = new ApiResponse();
        List<ApiWeekCourseModel> resultList = new ArrayList<>();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            //没有日期的话默认当前日期
            LocalDate tempDate = Optional.ofNullable(apiWeekCourseModel.getDate()).orElse(LocalDate.now());
            List<TCourse> courseList = courseService.list(new QueryWrapper<TCourse>()
                    //大于等于date
                    .ge("date", tempDate).ne("type", TCourseType.NOT_REGISTER.getCode()).orderByAsc("date", "lesson"));
            courseList.stream().collect(Collectors.groupingBy(TCourse::getDate)).forEach((date, tCourses) -> {
                ApiWeekCourseModel tempApiWeekCourseModel = new ApiWeekCourseModel();
                tempApiWeekCourseModel.setDate(date);
                tempApiWeekCourseModel.setWeekday(ChineseDayOfWeek.of(date.getDayOfWeek().getValue()).getDesc());
                tempApiWeekCourseModel.setList(tCourses);
                resultList.add(tempApiWeekCourseModel);
            });
            //排序
            resultList.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(resultList);

        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    @PostMapping("/getCoursesByID")
    public ApiResponse getCoursesByID(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none") String openid, @RequestBody TCourse course) {
        logger.info("getCoursesByID::openid = [{}], id = [{}]", openid, course.getId());
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional.ofNullable(course.getId()).ifPresentOrElse(idInteger -> {
                Optional.ofNullable(courseService.getById(idInteger)).ifPresentOrElse(tCourse -> {
                    apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                    apiResponse.setData(tCourse);
                }, () -> {
                    throw new RuntimeException("此id的数据为空");
                });
            }, () -> {
                throw new RuntimeException("id为空");
            });
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    //修改课程信息
    @PostMapping("/updateCourse")
    public ApiResponse updateCourse(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none") String openid, @RequestBody TCourse course) {
        logger.info("updateCourse::openid = [{}], course = [{}]", openid, course);
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional.ofNullable(course).ifPresentOrElse(idInteger -> {
                Optional.ofNullable(courseService.getById(course.getId())).ifPresentOrElse(tCourse -> {
                    tCourse.setCourseName(course.getCourseName());
                    //如果原状态是未注册状态，则修改为未上状态
                    if (tCourse.getType().equals(TCourseType.NOT_REGISTER)) {
                        tCourse.setType(TCourseType.NOT_START);
                    } else {
                        tCourse.setType(course.getType());
                    }
                    tCourse.setMaximum(course.getMaximum());
                    tCourse.setStartTime(course.getStartTime());
                    tCourse.setEndTime(course.getEndTime());
                    if (courseService.updateById(tCourse)) {
                        apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                        apiResponse.setData(tCourse);
                    } else {
                        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
                        apiResponse.setErrorMsg("更新失败");
                    }
                }, () -> {
                    throw new RuntimeException("此id的数据为空");
                });
            }, () -> {
                throw new RuntimeException("此id的数据为空");
            });
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    @PostMapping("/setFinished")
    public ApiResponse setFinished(@RequestBody TCourse course) {
        logger.info("updateCourse:: course = [{}]", course);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(course.getId()).orElseThrow(() -> new RuntimeException("id为空"));
            Optional.ofNullable(courseService.getById(course.getId())).ifPresentOrElse(tCourse -> {
                courseService.setCourseFinished(course.getId());
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setErrorMsg(Constant.APIRESPONSE_SUCCESS_MSG);
                apiResponse.setData(tCourse);
            }, () -> {
                throw new RuntimeException("无相关课程");
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    @PostMapping("/setStartSigningIn")
    public ApiResponse setStartSigningIn(@RequestBody TCourse course) {
        logger.info("updateCourse:: course = [{}]", course);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(course.getId()).orElseThrow(() -> new RuntimeException("id为空"));
            Optional.ofNullable(courseService.getById(course.getId())).ifPresentOrElse(tCourse -> {
                if (courseService.lambdaUpdate().set(TCourse::getType, TCourseType.START_SIGNING_IN.getCode())
                        .eq(TCourse::getId, course.getId())
                        .update()) {
                    apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                    apiResponse.setErrorMsg(Constant.APIRESPONSE_SUCCESS_MSG);
                    apiResponse.setData(tCourse);
                } else {
                    throw new RuntimeException("更新失败");
                }
            }, () -> {
                throw new RuntimeException("无相关课程");
            });

        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }


}
