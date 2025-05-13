package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.database.domain.TSysParams;
import com.chenxu.physical.theatre.database.service.TSysParamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author mamingze
 * @version 1.0
 * @title SystemController
 * @description
 * @create 2025/5/13 20:28
 */
@RestController
@RequestMapping("/system")
public class SystemController {
    private static final Logger logger = LoggerFactory.getLogger(SystemController.class);
    @Autowired
    private TSysParamsService tSysParamsService;

    //根据param_name获取param_value
    @PostMapping("/getByParamName")
    public ApiResponse getByParamName(@RequestParam String paramName) {
        logger.info("getByParamName::paramName = [{}]", paramName);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(paramName).orElseThrow(() -> new RuntimeException("paramName为空"));
            //按照分号符进行分割
            String[] paramNames = paramName.split(";");
            if (paramNames.length > 0) {
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(tSysParamsService.list(new QueryWrapper<TSysParams>()
                        .in("param_name", paramNames)));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }

        return apiResponse;
    }

}
