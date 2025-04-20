package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiOverviewOfCourseNumberModel;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.dto.ApiWeekCourseModel;
import com.chenxu.physical.theatre.database.constant.ChineseDayOfWeek;
import com.chenxu.physical.theatre.database.constant.TAppointmentInfoTypeEnum;
import com.chenxu.physical.theatre.database.constant.TCourseOrderStatus;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.domain.TCourseOrder;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import com.chenxu.physical.theatre.database.service.TCourseOrderService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
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
    TCourseOrderService courseOrderService;

    //获取全部可预约课程,去除已经预约过的课程,日期不传默认今天,查询七天的课程
    @PostMapping("/getBookableCoursesByUserid")
    public ApiResponse getBookableCoursesByUserid(@RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("getBookableCoursesByUserid::appointmentInfo = [{}]", appointmentInfo);
        ApiResponse apiResponse = new ApiResponse();
        List<ApiWeekCourseModel> resultList = new ArrayList<>();
        try {
            LocalDate tempDate = Optional.ofNullable(appointmentInfo.getDate()).orElse(LocalDate.now());
            Optional.ofNullable(appointmentInfo.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));
            List<TCourse> courses = courseService
                    .getBookableCoursesWithAppointmentInfoByUserid(appointmentInfo.getUserId(), tempDate, tempDate.plusDays(6));
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
                resultList.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
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
                //排序,增肌修改文字
                resultList.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
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
            Optional.ofNullable(courseOrderService.list(new QueryWrapper<TCourseOrder>().eq("user_id", courseOrder.getUserId()).eq("status", TCourseOrderStatus.NORMAL.getCode()))).ifPresentOrElse(tCourseOrderList -> {
                AtomicInteger totalCourseNumber = new AtomicInteger();
                //遍历所有有效订单的CourseNumbe作为总数量
                tCourseOrderList.forEach(tCourseOrder -> {
                    //原子加和
                    totalCourseNumber.addAndGet(tCourseOrder.getCourseNumber());
                });
                apiOverviewOfCourseNumberModel.setTotalCourseNumber(totalCourseNumber.get());
                //查询该用户的所有预约信息
                List<TAppointmentInfo> tAppointmentInfoList = appointmentInfoService.getAllAppointmentInfosByUserId(courseOrder.getUserId());
                //过滤所有已学和已签到的预约信息
                List<TAppointmentInfo> learnedAppointmentInfoList = tAppointmentInfoList.stream().filter(tAppointmentInfo -> tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.LEARNED) || tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.SIGNED)).collect(Collectors.toList());
                apiOverviewOfCourseNumberModel.setLearnedNumber(learnedAppointmentInfoList.size());
                apiOverviewOfCourseNumberModel.setLearnedCourseList(learnedAppointmentInfoList);
                //过滤所有已预约和已预约未签到的预约信息
                List<TAppointmentInfo> appointedCourseList = tAppointmentInfoList.stream().filter(tAppointmentInfo -> tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.APPOINTED) || tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.NOT_SIGNED)).collect(Collectors.toList());
                apiOverviewOfCourseNumberModel.setAppointedCourseList(appointedCourseList);
                apiOverviewOfCourseNumberModel.setAppointedNumber(appointedCourseList.size());
                apiOverviewOfCourseNumberModel.setTotalCourseList(tAppointmentInfoList);

                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(apiOverviewOfCourseNumberModel);
            }, () -> {
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(apiOverviewOfCourseNumberModel);
            });
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
            Optional.ofNullable(appointmentInfo.getUserId()).ifPresentOrElse(userId -> {
                //查询当前用户购买的课时
                Optional.ofNullable(courseOrderService.list(new QueryWrapper<TCourseOrder>().eq("user_id", userId).eq("status", TCourseOrderStatus.NORMAL.getCode()))).ifPresentOrElse(tCourseOrderList -> {

                    if (tCourseOrderList.size() == 0) {
                        throw new RuntimeException("该用户没有可用课时订单");
                    }
                    //有订单消息
                    AtomicInteger totalCourseNumber = new AtomicInteger();
                    tCourseOrderList.forEach(tCourseOrder -> {
                        //原子加和
                        totalCourseNumber.addAndGet(tCourseOrder.getCourseNumber());
                    });
                    if (totalCourseNumber.get() <= 0) {
                        throw new RuntimeException("该用户可用课时为零");
                    }
                    //到这里保证有可用课时,查询所有已预约,已签到和已学的预约信息,
                    Optional.ofNullable(appointmentInfoService.list(new QueryWrapper<TAppointmentInfo>().eq("user_id", userId).in("type", TAppointmentInfoTypeEnum.APPOINTED.getCode(), TAppointmentInfoTypeEnum.LEARNED.getCode(), TAppointmentInfoTypeEnum.SIGNED.getCode())//已预约

                    )).ifPresentOrElse(appointmentInfoList -> {

                        //有相关预约结果,在查询课程订单表
                        //遍历所有有效订单的CourseNumbe作为总数量
                        //判断有效的预约信息是否大于等于课程订单的课程数量
                        if (appointmentInfoList.size() >= totalCourseNumber.get()) {
                            //课时用完无法预约
                            throw new RuntimeException("课时用完无法预约");
                        } else {
                            //可以预约
                            Optional.ofNullable(appointmentInfo.getCourseId()).ifPresentOrElse(courseId -> {
                                Optional.ofNullable(courseService.getById(courseId)).ifPresentOrElse(tCourse -> {
                                    //先查询是否已经预约过,有取消预约状态的会把状态改为已预约,
                                    //可预约默认还没开课,所以不会是已学,只可能是已预约或者取消预约
                                    //已预约的情况的信息先删除,在增加新的预约信息(保证了预约信息的唯一性)
                                    appointmentInfoService.remove(new QueryWrapper<TAppointmentInfo>().eq("course_id", courseId).eq("user_id", appointmentInfo.getUserId()));
                                    //新增预约信息
                                    if (appointmentInfo.getType() == null) {
                                        appointmentInfo.setType(TAppointmentInfoTypeEnum.APPOINTED);
                                    }
                                    if (appointmentInfoService.save(appointmentInfo)) {
                                        courseService.updateCourseBookedNumber(courseId);
                                        apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                                        apiResponse.setData(appointmentInfo);
                                    } else {
                                        throw new RuntimeException("预约信息写入表失败");
                                    }
                                }, () -> {
                                    throw new RuntimeException("课程不存在");
                                });
                            }, () -> {
                                throw new RuntimeException("courseId为空");
                            });

                        }
                    }, () -> {
                        throw new RuntimeException("");
                    });
                }, () -> {
                    //没有相关订单,代表没有买课
                    apiResponse.setCode(Constant.APIRESPONSE_FAIL);
                    throw new RuntimeException("该用户的课程信息查询失败");
                });

            }, () -> {
                throw new RuntimeException("userId为空");
            });

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
            Optional.ofNullable(appointmentInfoService.getOne(new QueryWrapper<TAppointmentInfo>().eq("course_id", appointmentInfo.getCourseId()).eq("user_id", appointmentInfo.getUserId()))).ifPresentOrElse(tempAppointment -> {
                Optional.ofNullable(courseService.getById(appointmentInfo.getCourseId())).ifPresentOrElse(course -> {
                    if (LocalDateTime.now().isAfter(course.getStartTime().minusHours(24))) {
                        //如果已经超过24小时,则不能取消预约
                        throw new RuntimeException("距离开始不满24小时,不能取消预约");
                    } else {
                        //如果小于24小时,则取消预约
                        tempAppointment.setType(TAppointmentInfoTypeEnum.CANCELED);
                        if (appointmentInfoService.updateById(tempAppointment)) {
                            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                            apiResponse.setData(tempAppointment);
                        }
                    }
                    courseService.updateCourseBookedNumber(appointmentInfo.getCourseId());
                }, () -> {
                    throw new RuntimeException("课程订单信息查询失败");
                });
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

}
