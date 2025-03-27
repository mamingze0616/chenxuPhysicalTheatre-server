package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.database.constant.TCourseType;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.service.TCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


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
    @Autowired
    private TCourseService courseService;

    //获取全部可预约课程
    @RequestMapping("/getAllAppointment")
    public ApiResponse getAllAppointment() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            //查询可预约课程
            List<TCourse> courses = courseService.list(new QueryWrapper<TCourse>()
                    //课程类型为未上
                    .eq("type", TCourseType.NOT_START.getCode())
                    //开始时间为当前时间之后
                    .ge("startTime", LocalDateTime.now())
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
}
