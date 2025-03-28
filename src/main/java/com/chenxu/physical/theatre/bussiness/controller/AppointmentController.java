package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import com.chenxu.physical.theatre.database.service.TCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


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

    @PostMapping("/getAppointmentInfoByCourseId")
    public ApiResponse getAppointmentInfoByCourseId(@RequestHeader(value = "X-WX-OPENID",
                                                            required = false,
                                                            defaultValue = "none")
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
}
