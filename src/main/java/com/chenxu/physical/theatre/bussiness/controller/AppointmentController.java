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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


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

    //获取全部可预约课程
    @PostMapping("/getAllAppointment")
    public ApiResponse getAllAppointment() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            //查询可预约课程
            List<TCourse> courses = courseService.list(new QueryWrapper<TCourse>()
                    //课程类型为未上
                    .eq("type", TCourseType.NOT_START.getCode())
                    //开始时间为当前时间之后
                    .ge("start_time", LocalDateTime.now())
                    //按照日期和课时升序
                    .orderByAsc("date", "lesson"));
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(courses);
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
     * @param course
     * @return
     */
    @PostMapping("/getAppointmentByDate")
    public ApiResponse getAppointmentByDate(
            @RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
            String openid, @RequestBody TCourse course) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional.ofNullable(course.getDate()).ifPresentOrElse(date -> {
                //查询可预约课程
                List<TCourse> courses = courseService.list(new QueryWrapper<TCourse>()
                        //课程类型为未上
                        .eq("type", TCourseType.NOT_START.getCode())
                        //开始时间为当前时间之后
                        .ge("date", date)
                        //按照日期和课时升序
                        .orderByAsc("date", "lesson"));
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(courses);
            }, () -> {
                throw new RuntimeException("date为空");
            });
        } catch (Exception e) {
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    @PostMapping("/getAppointmentInfoByCourseId")
    public ApiResponse getAppointmentInfoByCourseId(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                                    String openid,
                                                    TAppointmentInfo appointmentInfo) {
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
    public ApiResponse getOverviewOfCourseNumberInfoAndAppointmentInfo(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                                                       String openid, @RequestBody TCourseOrder courseOrder) {
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
                Optional.ofNullable(courseOrderService.list(new QueryWrapper<TCourseOrder>().eq("user_id", userId).eq("status", TCourseOrderStatus.NORMAL.getCode())))
                        .ifPresentOrElse(tCourseOrderList -> {
                            //
                            AtomicInteger totalCourseNumber = new AtomicInteger();
                            //遍历所有有效订单的CourseNumbe作为总数量
                            tCourseOrderList.forEach(tCourseOrder -> {
                                //原子加和
                                totalCourseNumber.addAndGet(tCourseOrder.getCourseNumber());
                            });
                            apiOverviewOfCourseNumberModel.setTotalCourseNumber(totalCourseNumber.get());
                            //查询该用户的所有预约信息
                            List<TAppointmentInfo> tAppointmentInfoList =
                                    appointmentInfoService.list(new QueryWrapper<TAppointmentInfo>().eq("user_id", userId));
                            //过滤所有已学的预约信息
                            apiOverviewOfCourseNumberModel.setLearnedNumber((int) tAppointmentInfoList.stream().filter(tAppointmentInfo ->
                                    tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.LEARNED.getCode())
                                            || tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.SIGNED.getCode())).count());
                            //过滤所有已预约的预约信息
                            apiOverviewOfCourseNumberModel.setAppointedNumber((int) tAppointmentInfoList.stream().filter(tAppointmentInfo ->
                                    tAppointmentInfo.getType().equals(TAppointmentInfoTypeEnum.APPOINTED.getCode())).count());

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
     * 预约某个课程,或者取消预约某个课程,不论怎样都是新增数据
     *
     * @param openid
     * @return
     */
    @PostMapping("/appointmentByCourseId")
    public ApiResponse appointmentByCourseId(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                             String openid,
                                             @RequestBody TAppointmentInfo appointmentInfo) {
        logger.info("appointmentByCourseId::openid = [{}], appointmentInfo = [{}]", openid, appointmentInfo);
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional.ofNullable(appointmentInfo.getCourseId()).ifPresentOrElse(courseId -> {
                Optional.ofNullable(courseService.getById(courseId)).ifPresentOrElse(tCourse -> {
                    //先查询是否已经预约过,有取消预约状态的会把状态改为已预约
                    Optional.ofNullable(appointmentInfoService.list(new QueryWrapper<TAppointmentInfo>()
                            .eq("course_id", courseId)
                            .eq("user_id", appointmentInfo.getUserId()))).ifPresent(
                            appointmentInfoList -> {
                                //删掉旧的预约信息
                                appointmentInfoService.removeByIds(appointmentInfoList.stream()
                                        .map(TAppointmentInfo::getId).collect(Collectors.toList()));
                            });
                    //新增预约信息
                    if (appointmentInfo.getType() == null) {
                        appointmentInfo.setType(TAppointmentInfoTypeEnum.APPOINTED);
                    }
                    appointmentInfo.setCreateAt(LocalDateTime.now());
                    if (appointmentInfoService.save(appointmentInfo)) {
                        apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                        apiResponse.setData(appointmentInfo);
                    } else {
                        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
                        apiResponse.setErrorMsg("预约失败");
                    }
                }, () -> {
                    throw new RuntimeException("课程不存在");
                });
            }, () -> {
                throw new RuntimeException("courseId为空");
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }


}
