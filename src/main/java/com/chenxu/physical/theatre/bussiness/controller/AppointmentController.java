package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiOverviewOfCourseNumberModel;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.dto.ApiWeekCourseModel;
import com.chenxu.physical.theatre.bussiness.dto.achievement.ApiAchievementModel;
import com.chenxu.physical.theatre.bussiness.dto.achievement.ApiSkillTypeModel;
import com.chenxu.physical.theatre.bussiness.service.BookedCourseService;
import com.chenxu.physical.theatre.database.constant.ChineseDayOfWeek;
import com.chenxu.physical.theatre.database.constant.TAppointmentInfoTypeEnum;
import com.chenxu.physical.theatre.database.domain.*;
import com.chenxu.physical.theatre.database.service.TAchievementService;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import com.chenxu.physical.theatre.database.service.TCourseService;
import com.chenxu.physical.theatre.database.service.TUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.chenxu.physical.theatre.bussiness.constant.Constant.APIRESPONSE_SUCCESS_MSG;


/**
 * @author mamingze
 * @version 1.0
 * @title AppointmentController
 * @description
 * @create 2025/3/26 22:01
 */
@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
    @Autowired
    TCourseService courseService;
    @Autowired
    TUserService userService;
    @Autowired
    TAppointmentInfoService appointmentInfoService;
    @Autowired
    BookedCourseService bookedCourseService;
    @Autowired
    TAchievementService tAchievementService;

    //获取全部可预约课程,去除已经预约过的课程,日期不传默认今天,查询七天的课程
    @PostMapping("/getBookableCoursesByUserid")
    public ApiResponse getBookableCoursesByUserid(@RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("getBookableCoursesByUserid::appointmentInfo = [{}]", appointmentInfo);
        ApiResponse apiResponse = new ApiResponse();
        List<ApiWeekCourseModel> resultList = new ArrayList<>();
        try {
            LocalDate tempDate = Optional.ofNullable(appointmentInfo.getDate()).orElse(LocalDate.now());
            Optional.ofNullable(appointmentInfo.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));
            List<TCourse> courses = courseService.getBookableCoursesWithAppointmentInfoByUserid(appointmentInfo.getUserId(), tempDate, tempDate.plusDays(6));
            if (courses.isEmpty()) {
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(resultList);
            } else {
                courses.stream().collect(Collectors.groupingBy(TCourse::getDate)).forEach((date, tCourses) -> {
                    ApiWeekCourseModel apiWeekCourseModel = new ApiWeekCourseModel();
                    apiWeekCourseModel.setDate(date);
                    apiWeekCourseModel.setWeekday(ChineseDayOfWeek.of(date.getDayOfWeek().getValue()).getDesc());
                    apiWeekCourseModel.setList(tCourses);
                    resultList.add(apiWeekCourseModel);
                });
                //排序
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(resultList);
            }
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }

        return apiResponse;
    }

    /**
     * 获取用户预约过,但没上和没签到的课程,时间是一周内,时间不传默认传本日
     *
     * @param appointmentInfo
     * @return 按照天数分类, 附带课程预约信息
     * @throws
     */
    @PostMapping("/getAleardyBookedCoursersWithAppointmentInfoByUserid")
    public ApiResponse getAleardyBookedCoursersWithAppointmentInfoByUserid(@RequestBody TAppointmentInfo appointmentInfo) {
        ApiResponse apiResponse = new ApiResponse();
        List<ApiWeekCourseModel> resultList = new ArrayList<>();
        try {

            LocalDate tempDate = Optional.ofNullable(appointmentInfo.getDate()).orElse(LocalDate.now());
            Optional.ofNullable(appointmentInfo.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));
            List<TCourse> courses = courseService.getAleardyBookedCoursersWithAppointmentInfoByUserid(appointmentInfo.getUserId(), tempDate, tempDate.plusDays(6));
            if (courses.isEmpty()) {
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(resultList);
            } else {
                courses.stream().collect(Collectors.groupingBy(TCourse::getDate)).forEach((date, tCourses) -> {
                    ApiWeekCourseModel apiWeekCourseModel = new ApiWeekCourseModel();
                    apiWeekCourseModel.setDate(date);
                    apiWeekCourseModel.setWeekday(ChineseDayOfWeek.of(date.getDayOfWeek().getValue()).getDesc());
                    apiWeekCourseModel.setList(tCourses);
                    resultList.add(apiWeekCourseModel);
                });
                //排序
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(resultList);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    /**
     * 根据日期查询课程 ,从date开始查询
     *
     * @param appointmentInfo
     * @return
     */
    @PostMapping("/getAppointmentInfosByUserIdAndDate")
    public ApiResponse getAppointmentInfosByUserIdAndDate(@RequestBody TAppointmentInfo appointmentInfo) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional.ofNullable(appointmentInfo.getUserId()).ifPresentOrElse(userID -> {
                LocalDate querydate = Optional.ofNullable(appointmentInfo.getDate()).orElse(LocalDate.now());
                //查询该用户的预约表所有状态的的信息
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(appointmentInfoService.getAppointmentInfosByUserIdAndDate(appointmentInfo.getUserId(), querydate));
            }, () -> {
                throw new RuntimeException("userId为空");
            });
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    @PostMapping("/getAppointmentInfoByCourseId")
    public ApiResponse getAppointmentInfoByCourseId(@RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("getAppointmentInfoByCoursrId:: appointmentInfo = [{}]", appointmentInfo);
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional.ofNullable(appointmentInfo.getCourseId()).orElseThrow(() -> new RuntimeException("课程Id为空"));
            List<TAppointmentInfo> list = appointmentInfoService.getAppointmentInfoByCourseId(appointmentInfo.getCourseId());
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }

        return apiResponse;
    }

    /**
     * 查询已上,已预约,总可数量三项信息
     *
     * @param courseOrder
     * @return
     */
    @PostMapping("getOverviewOfCourseNumberInfoAndAppointmentInfo")
    public ApiResponse getOverviewOfCourseNumberInfoAndAppointmentInfo(@RequestBody TCourseOrder courseOrder) {
        logger.info("getOverviewOfCourseNumberInfo:: courseOrder.userid = [{}]", courseOrder.getUserId());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        ApiOverviewOfCourseNumberModel apiOverviewOfCourseNumberModel = new ApiOverviewOfCourseNumberModel();
        apiOverviewOfCourseNumberModel.setTotalCourseNumber(0);
        apiOverviewOfCourseNumberModel.setLearnedNumber(0);
        apiOverviewOfCourseNumberModel.setAppointedNumber(0);
        apiResponse.setData(apiOverviewOfCourseNumberModel);
        try {
            Optional.ofNullable(courseOrder.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));

            //查询该用户的所有订单状态为正常的订单
            TUser user = Optional.ofNullable(userService.getById(courseOrder.getUserId())).orElseThrow(() -> new RuntimeException("userId为空"));
            apiOverviewOfCourseNumberModel.setTotalCourseNumber(user.getEffectiveCourseCount());
            //查询该用户的所有预约信息
            List<TAppointmentInfo> tAppointmentInfoList = appointmentInfoService.getAllAppointmentInfosByUserId(courseOrder.getUserId());
            //过滤所有已学和已签到的预约信息
            List<TAppointmentInfo> learnedAppointmentInfoList = tAppointmentInfoList.stream().filter(tAppointmentInfo -> tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.LEARNED) || tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.SIGNED)).collect(Collectors.toList());
            apiOverviewOfCourseNumberModel.setLearnedNumber(user.getCompletedCourseCount());
            apiOverviewOfCourseNumberModel.setLearnedCourseList(learnedAppointmentInfoList);
            //过滤所有已预约和已预约未签到的预约信息
            List<TAppointmentInfo> appointedCourseList = tAppointmentInfoList.stream().filter(tAppointmentInfo -> tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.APPOINTED) || tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.NOT_SIGNED)).collect(Collectors.toList());
            apiOverviewOfCourseNumberModel.setAppointedCourseList(appointedCourseList);
            apiOverviewOfCourseNumberModel.setAppointedNumber(appointedCourseList.size());
            apiOverviewOfCourseNumberModel.setTotalCourseList(tAppointmentInfoList);


            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(apiOverviewOfCourseNumberModel);

        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg("获取失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }

    /**
     * 预约某个课程,先判断已经学过的课程+已预约+已签到的课程跟总可用课时的关系,大于等于就无法预约
     * 在删除能搜索到的同款课程的预约信息,再进行预约管理
     *
     * @param
     * @return
     */
    @PostMapping("/doAppointmentByCourseId")
    public ApiResponse doAppointmentByCourseId(@RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("doAppointmentByCourseId::appointmentInfo = [{}]", appointmentInfo);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(appointmentInfo.getCourseId()).orElseThrow(() -> new RuntimeException("课程Id为空"));
            Optional.ofNullable(appointmentInfo.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));
            //查询当前用户购买的课时
            //到这里保证有可用课时,查询预约信息,
            if (bookedCourseService.doBookedCourse(appointmentInfo)) {
                courseService.updateCourseBookedNumber(appointmentInfo.getCourseId());
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(appointmentInfo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    /**
     * 取消预约某个课程,如果在24小时不能取消预约
     *
     * @param
     * @return
     */
    @PostMapping("/cancelCourseAppointment")
    public ApiResponse cancelCourseAppointment(@RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("cancelCourseAppointment:: appointmentInfo = [{}]", appointmentInfo);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        apiResponse.setErrorMsg(APIRESPONSE_SUCCESS_MSG);
        try {
            Optional.ofNullable(appointmentInfo.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));
            Optional.ofNullable(appointmentInfo.getCourseId()).orElseThrow(() -> new RuntimeException("courseId为空"));
            if (bookedCourseService.cancelCourseAppointment(appointmentInfo)) {
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(appointmentInfo);
            }
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    /**
     * 签到某个课程
     *
     * @param
     * @param appointmentInfo 预约信息,带有课程id和用户id
     * @return
     */
    @PostMapping("/signInCourseAppointment")
    public ApiResponse signInCourseAppointment(@RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("signInCourseAppointment:: appointmentInfo = [{}]", appointmentInfo);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(appointmentInfo.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));
            Optional.ofNullable(appointmentInfo.getCourseId()).orElseThrow(() -> new RuntimeException("courseId为空"));
            Optional.ofNullable(appointmentInfoService.getOne(new QueryWrapper<TAppointmentInfo>().eq("course_id", appointmentInfo.getCourseId()).eq("user_id", appointmentInfo.getUserId()))).ifPresentOrElse(tempAppointment -> {
                if (tempAppointment.getType().equals(TAppointmentInfoTypeEnum.APPOINTED)) {
                    tempAppointment.setType(TAppointmentInfoTypeEnum.SIGNED);
                    if (appointmentInfoService.updateById(tempAppointment)) {
                        apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                        apiResponse.setErrorMsg(APIRESPONSE_SUCCESS_MSG);
                        apiResponse.setData(tempAppointment);
                    } else {
                        throw new RuntimeException("签到失败");
                    }
                } else if (tempAppointment.getType().equals(TAppointmentInfoTypeEnum.SIGNED)) {
                    throw new RuntimeException("该课程已经签到过了");
                } else {
                    throw new RuntimeException("该课程状态无法签到!");
                }
            }, () -> {
                throw new RuntimeException("该用户没有预约过该课程");
            });
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    /**
     * 获取课程表和课程预约表联合查询的预约信息,查询条件为日期,不传日期则传全部课程的预约信息(包括往期课程)
     *
     * @param
     * @param course 课程信息,带有日期查询字段,不传默认查询全部日期
     * @return
     */
    @PostMapping("/getCourseInfoWithAppointmentInfoList")
    public ApiResponse getCourseInfoWithAppointmentInfoList(@RequestBody TCourse course) {
        logger.info("getAppointmentInfoListWithCourse:: course = [{}]", course);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            PageDTO<TCourse> quertPage = new PageDTO<TCourse>(Optional.ofNullable(course.getCurrent()).orElse(1), Optional.ofNullable(course.getSize()).orElse(10));
            apiResponse.setData(courseService.selectPageTCourseList(quertPage, course));
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setErrorMsg(APIRESPONSE_SUCCESS_MSG);
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;

    }

    /**
     * 获取人员表和课程预约表联合查询的预约信息,查询条件为人员ID,不传日期则传全部人员的信息
     *
     * @param
     * @param user 人员信息
     * @return
     */
    @PostMapping("/getUserInfoWithAppointmentInfoList")
    public ApiResponse getUserInfoWithAppointmentInfoList(@RequestBody TUser user) {
        logger.info("getUserInfoWithAppointmentInfoList:: course = [{}]", user);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            PageDTO<TUser> quertPage = new PageDTO<>(Optional.ofNullable(user.getCurrent()).orElse(1), Optional.ofNullable(user.getSize()).orElse(10));
            apiResponse.setData(userService.selectPageTUserWithAppointmentInfoList(quertPage, user));
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setErrorMsg(APIRESPONSE_SUCCESS_MSG);
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;

    }

    //管理员修改预约信息的type
    @PostMapping("/updateAppointmentInfoTypByAdmin")
    public ApiResponse updateAppointmentInfoTypByAdmin(@RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("updateAppointmentInfoTypByAdmin:: appointmentInfo = [{}]", appointmentInfo);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(appointmentInfo.getId()).orElseThrow(() -> new RuntimeException("id为空"));
            Optional.ofNullable(appointmentInfoService.getById(appointmentInfo.getId())).ifPresentOrElse(tmpAppointment -> {
                appointmentInfoService.lambdaUpdate().set(TAppointmentInfo::getType, appointmentInfo.getType()).eq(TAppointmentInfo::getId, appointmentInfo.getId()).update();
                courseService.updateCourseBookedNumber(tmpAppointment.getCourseId());
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setErrorMsg(APIRESPONSE_SUCCESS_MSG);
            }, () -> {
                throw new RuntimeException("无相关预约信息");
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    /**
     * 获取个人成就列表
     *
     * @return
     */
    @PostMapping("/getAchievementListById")
    public ApiResponse getAchievementListById(@RequestBody TUser user) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(user.getId()).orElseThrow(() -> new RuntimeException("id为空"));
            List<TAppointmentInfo> appointmentInfos = appointmentInfoService.getAllAppointmentInfosByUserId(user.getId());
            //按照achievementId分组
            Map<String, List<TAppointmentInfo>> appointmentInfosMap =
                    appointmentInfos.stream()
                            .collect(Collectors.groupingBy(TAppointmentInfo::getAchievementId));
            List<TAchievement> achievementList = tAchievementService.list();
            //按照skillType分组
            List<ApiSkillTypeModel> apiSkillTypeModels = new ArrayList<>();
            achievementList.stream().collect(Collectors.groupingBy(TAchievement::getSkillType))
                    .forEach((skillType, tAchievements) -> {
                        ApiSkillTypeModel apiSkillTypeModel = new ApiSkillTypeModel();
                        apiSkillTypeModel.setLabel(tAchievements.get(0).getSkillTypeName());
                        apiSkillTypeModel.setValue(String.valueOf(skillType));
                        apiSkillTypeModel.setChildren(tAchievements.stream().map(item -> {
                            ApiAchievementModel apiAchievementModel = new ApiAchievementModel();
                            apiAchievementModel.setLabel(item.getSpecificName());
                            apiAchievementModel.setValue(String.valueOf(item.getId()));
                            if (appointmentInfosMap.containsKey(String.valueOf(item.getId()))) {
                                apiAchievementModel.setAppointmentInfos(appointmentInfosMap.get(String.valueOf(item.getId())));
                            }
                            return apiAchievementModel;
                        }).collect(Collectors.toList()));
                        apiSkillTypeModels.add(apiSkillTypeModel);
                    });
            apiResponse.setData(apiSkillTypeModels);
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

}
