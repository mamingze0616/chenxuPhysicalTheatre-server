package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiOverviewOfCourseNumberModel;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.database.constant.TAppointmentInfoTypeEnum;
import com.chenxu.physical.theatre.database.constant.TCourseOrderStatus;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.domain.TCourseOrder;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import com.chenxu.physical.theatre.database.service.TCourseOrderService;
import com.chenxu.physical.theatre.database.service.TCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private TCourseService courseService;
    @Autowired
    TAppointmentInfoService appointmentInfoService;
    @Autowired
    TCourseOrderService courseOrderService;

    //获取全部可预约课程,去除已经预约过的课程,日期不传默认今天,
    @PostMapping("/getBookableCoursesByUseridAndDate")
    public ApiResponse getBookableCoursesByUseridAndDate(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                                         String openid, @RequestBody TAppointmentInfo appointmentInfo) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional.ofNullable(appointmentInfo.getUserId()).ifPresentOrElse(userId -> {
                LocalDate querydate = Optional.ofNullable(appointmentInfo.getDate()).orElse(LocalDate.now());
                List<TCourse> courses = courseService.list(new QueryWrapper<TCourse>()
                        //课程类型为未上
                        .eq("type", TCourseType.NOT_START.getCode())
                        //开始时间为当前时间之后
                        .ge("start_time", LocalDateTime.now())
                        //时间在querydate之前
                        .ge("date", querydate)
                        //按照日期和课时升序
                        .orderByAsc("date", "lesson"));
//                if (courses.size() == 0) {
//                    apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
//                    apiResponse.setErrorMsg("没有可预约课程");
//                    apiResponse.setData(courses);
//                }
                //查找1:已预约;3:已学;4:已签到的预约信息
                List<TAppointmentInfo> appointmentInfos = appointmentInfoService.list(new QueryWrapper<TAppointmentInfo>()
                        .eq("user_id", userId)
                        .in("type", TAppointmentInfoTypeEnum.APPOINTED.getCode(),
                                TAppointmentInfoTypeEnum.LEARNED.getCode(),
                                TAppointmentInfoTypeEnum.SIGNED.getCode()));
                logger.info("是否有可预约课程被删除:{}", courses.removeIf(course ->
                        appointmentInfos.stream().anyMatch(appointmentInfo1 ->
                                appointmentInfo1.getCourseId().equals(course.getId()))));
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(courses);

            }, () -> {
                throw new RuntimeException("userId为空");
            });
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }

        return apiResponse;
    }


    /**
     * 根据日期查询课程 ,从date开始查询
     *
     * @param openid
     * @param appointmentInfo
     * @return
     */
    @PostMapping("/getAppointmentInfosByUserIdAndDate")
    public ApiResponse getAppointmentInfosByUserIdAndDate(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                                          String openid, @RequestBody TAppointmentInfo appointmentInfo) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional.ofNullable(appointmentInfo.getUserId()).ifPresentOrElse(userID -> {
                LocalDate querydate = Optional.ofNullable(appointmentInfo.getDate()).orElse(LocalDate.now());
                //查询该用户的预约表所有状态的的信息
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(appointmentInfoService
                        .getAppointmentInfosByUserIdAndDate(appointmentInfo.getUserId(), querydate));
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
    public ApiResponse getAppointmentInfoByCourseId(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none") String openid,
                                                    @RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("getAppointmentInfoByCoursrId::openid = [{}], appointmentInfo = [{}]", openid, appointmentInfo);
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional.ofNullable(appointmentInfo.getCourseId()).ifPresentOrElse(CourseIdInteger -> {
                List<TAppointmentInfo> list = appointmentInfoService.getAppointmentInfoByCourseId(CourseIdInteger);
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(list);
            }, () -> {
                throw new RuntimeException("courseId为空");
            });
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }

        return apiResponse;
    }

    /**
     * 查询已上,已预约,总可数量三项信息
     *
     * @param openid
     * @param courseOrder
     * @return
     */
    @PostMapping("getOverviewOfCourseNumberInfoAndAppointmentInfo")
    public ApiResponse getOverviewOfCourseNumberInfoAndAppointmentInfo(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none") String openid,
                                                                       @RequestBody TCourseOrder courseOrder) {
        logger.info("getOverviewOfCourseNumberInfo::openid = [{}], courseOrder = [{}]", openid, courseOrder);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        ApiOverviewOfCourseNumberModel apiOverviewOfCourseNumberModel = new ApiOverviewOfCourseNumberModel();
        apiOverviewOfCourseNumberModel.setTotalCourseNumber(0);
        apiOverviewOfCourseNumberModel.setLearnedNumber(0);
        apiOverviewOfCourseNumberModel.setAppointedNumber(0);
        apiResponse.setData(apiOverviewOfCourseNumberModel);
        try {
            Optional.ofNullable(courseOrder.getUserId()).ifPresentOrElse(userId -> {
                //查询该用户的所有订单状态为正常的订单
                Optional.ofNullable(courseOrderService.list(new QueryWrapper<TCourseOrder>().eq("user_id", userId).eq("status", TCourseOrderStatus.NORMAL.getCode()))).ifPresentOrElse(tCourseOrderList -> {
                    //
                    AtomicInteger totalCourseNumber = new AtomicInteger();
                    //遍历所有有效订单的CourseNumbe作为总数量
                    tCourseOrderList.forEach(tCourseOrder -> {
                        //原子加和
                        totalCourseNumber.addAndGet(tCourseOrder.getCourseNumber());
                    });
                    apiOverviewOfCourseNumberModel.setTotalCourseNumber(totalCourseNumber.get());
                    //查询该用户的所有预约信息
                    List<TAppointmentInfo> tAppointmentInfoList = appointmentInfoService.getAllAppointmentInfosByUserId(userId);
                    //过滤所有已学的预约信息
                    List<TAppointmentInfo> learnedAppointmentInfoList = tAppointmentInfoList.stream().filter(tAppointmentInfo ->
                                    tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.LEARNED) || tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.SIGNED))
                            .collect(Collectors.toList());
                    apiOverviewOfCourseNumberModel.setLearnedNumber(learnedAppointmentInfoList.size());
                    apiOverviewOfCourseNumberModel.setLearnedCourseList(learnedAppointmentInfoList);
                    //过滤所有已预约的预约信息
                    List<TAppointmentInfo> appointedCourseList = tAppointmentInfoList.stream().filter(tAppointmentInfo -> tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.APPOINTED))
                            .collect(Collectors.toList());
                    apiOverviewOfCourseNumberModel.setAppointedCourseList(appointedCourseList);
                    apiOverviewOfCourseNumberModel.setAppointedNumber(appointedCourseList.size());
                    //合并已学和已预约的预约信息
                    tAppointmentInfoList.addAll(learnedAppointmentInfoList);
                    apiOverviewOfCourseNumberModel.setTotalCourseList(tAppointmentInfoList);

                    apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                    apiResponse.setData(apiOverviewOfCourseNumberModel);
                }, () -> {
                    apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                    apiResponse.setData(apiOverviewOfCourseNumberModel);
                });
            }, () -> {
                throw new RuntimeException("userId为空");
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
     * @param openid
     * @return
     */
    @PostMapping("/doAppointmentByCourseId")
    public ApiResponse doAppointmentByCourseId(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                               String openid,
                                               @RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("doAppointmentByCourseId::openid = [{}], appointmentInfo = [{}]", openid, appointmentInfo);
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
                    Optional.ofNullable(appointmentInfoService.list(new QueryWrapper<TAppointmentInfo>().eq("user_id", userId).eq("type", TAppointmentInfoTypeEnum.APPOINTED.getCode())//已预约
                            .or().eq("type", TAppointmentInfoTypeEnum.LEARNED.getCode())//已学
                            .or().eq("type", TAppointmentInfoTypeEnum.SIGNED.getCode())//已签到
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
                                    Optional.ofNullable(appointmentInfoService.list(new QueryWrapper<TAppointmentInfo>().eq("course_id", courseId).eq("user_id", appointmentInfo.getUserId()))).ifPresent(appointmentInfoListOfSameCourseId -> {
                                        //删掉旧s预约信息
                                        appointmentInfoService.removeByIds(appointmentInfoListOfSameCourseId.stream().map(TAppointmentInfo::getId).collect(Collectors.toList()));
                                    });
                                    //新增预约信息
                                    if (appointmentInfo.getType() == null) {
                                        appointmentInfo.setType(TAppointmentInfoTypeEnum.APPOINTED);
                                    }
                                    if (appointmentInfoService.save(appointmentInfo)) {
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
     * @param openid
     * @return
     */
    @PostMapping("/cancelCourseAppointment")
    public ApiResponse cancelCourseAppointment(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                               String openid,
                                               @RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("cancelCourseAppointment::openid = [{}], appointmentInfo = [{}]", openid, appointmentInfo);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        apiResponse.setErrorMsg(APIRESPONSE_SUCCESS_MSG);
        try {
            Optional.ofNullable(appointmentInfo.getUserId()).orElseThrow(() -> new RuntimeException("userId为空"));
            Optional.ofNullable(appointmentInfo.getCourseId()).orElseThrow(() -> new RuntimeException("courseId为空"));
            Optional.ofNullable(appointmentInfoService.getOne(new QueryWrapper<TAppointmentInfo>()
                    .eq("course_id", appointmentInfo.getCourseId())
                    .eq("user_id", appointmentInfo.getUserId()))).ifPresentOrElse(tempAppointment -> {
                //
                if (TAppointmentInfoTypeEnum.CANCELED.compareTo(tempAppointment.getType()) == 0) {
                    //如果已经取消预约,则直接返回
                    apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                    apiResponse.setData(tempAppointment);
                } else {
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
                    }, () -> {
                        throw new RuntimeException("课程订单信息查询失败");
                    });
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


}
