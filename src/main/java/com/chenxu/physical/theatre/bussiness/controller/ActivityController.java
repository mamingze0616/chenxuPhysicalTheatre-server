package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.database.domain.TActivity;
import com.chenxu.physical.theatre.database.service.TActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mamingze
 * @version 1.0
 * @title ActivityController
 * @description
 * @create 2025/5/16 20:26
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);
    @Autowired
    TActivityService tActivityService;

    //新增一个活动
    @PostMapping("/addAcitivity")
    public ApiResponse addAcitivity(@RequestBody TActivity activity) {
        logger.info("addAcitivity::activity = [{}]", activity);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            tActivityService.save(activity);
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setErrorMsg(Constant.APIRESPONSE_SUCCESS_MSG);
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }

    @PostMapping("/getActivityList")
    public ApiResponse getActivityList() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<TActivity> tActivityList = tActivityService.list();
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
//            apiResponse.setErrorMsg(Constant.APIRESPONSE_SUCCESS_MSG);
            apiResponse.setData(tActivityList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }
}
