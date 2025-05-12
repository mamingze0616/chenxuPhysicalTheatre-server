package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.service.MemberShipService;
import com.chenxu.physical.theatre.bussiness.util.IpUtils;
import com.chenxu.physical.theatre.database.domain.TUserOrder;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author mamingze
 * @version 1.0
 * @title MemshipContoller
 * @description
 * @create 2025/5/12 14:49
 */
@RestController
@RequestMapping("/memship")
public class MemshipContoller {
    private static final Logger logger = LoggerFactory.getLogger(MemshipContoller.class);
    @Autowired
    MemberShipService memberShipService;

    /**
     * 升级成为会员,下单阶段
     *
     * @return
     */
    @PostMapping("/preUpgrade")
    public ApiResponse preUpgrade(@RequestBody TUserOrder tUserOrder, HttpServletRequest request) {
        logger.info("preUpgrade::tUserOrder = [{}]", tUserOrder);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(tUserOrder.getUserId()).orElseThrow(() -> new RuntimeException("用户id为空"));
            Optional.ofNullable(tUserOrder.getAmount()).orElseThrow(() -> new RuntimeException("金额不能为空"));
            //要下一个会员升级订单的话要创建一个支付订单
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(memberShipService.preUpgrade(tUserOrder, IpUtils.getIp(request)));
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;


    }

}
