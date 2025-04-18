package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.dto.ApiWeekCourseModel;
import com.chenxu.physical.theatre.database.constant.ChineseDayOfWeek;
import com.chenxu.physical.theatre.database.constant.TCourseConstans;
import com.chenxu.physical.theatre.database.constant.TCourseStartTime;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.domain.TCourse;
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
     * 获取从date日期开始的两周内的课程信息
     * 如果某一天没课程就先新增课程
     *
     * @param openid
     * @param apiWeekCourseModel
     * @return
     */
    @PostMapping("/getTwoWeekCourses")
    public ApiResponse getTwoWeekCourses(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none") String openid, @RequestBody ApiWeekCourseModel apiWeekCourseModel) {
        logger.info("getTwoWeekCourses::openid = [{}], date = [{}]", openid, apiWeekCourseModel);
        //查询权限先不做
        ApiResponse apiResponse = new ApiResponse();
        //先按照日期查询课程
        try {
            //没有日期的话默认当前日期
            LocalDate tempDate = Optional.ofNullable(apiWeekCourseModel.getDate()).orElse(LocalDate.now());
            //先查询两个星期
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(checkTwoWeekCourses(tempDate));
        } catch (Exception e) {
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
            apiWeekCourseModel.setDate(plusDaysdate);
            apiWeekCourseModel.setWeekday(ChineseDayOfWeek.of(plusDaysdate.getDayOfWeek().getValue()).getDesc());
            //当天临时课程
            List<TCourse> tempList = list.stream().filter(c -> c.getDate().equals(plusDaysdate)).collect(Collectors.toList());
            if (tempList.size() == 0) {
                tempList = initEverdayCourse(plusDaysdate);
            } else {
                //暂时不处理其他情况
            }
            tempList.sort((o1, o2) -> o1.getLesson().compareTo(o2.getLesson()));
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
            course.setType(TCourseType.NOT_REGISTER);
            LocalDateTime startTime = date.atTime(TCourseStartTime.getStartTimeByCode(i));
            course.setStartTime(startTime);
            course.setEndTime(startTime.plusHours(1));
            courseService.save(course);
            list.add(course);
        }
        return list;

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

}
