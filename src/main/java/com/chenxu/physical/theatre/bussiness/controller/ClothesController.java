package com.chenxu.physical.theatre.bussiness.controller;

import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.service.ClothesService;
import com.chenxu.physical.theatre.bussiness.util.IpUtils;
import com.chenxu.physical.theatre.database.domain.TClothesOrder;
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
 * @title ClothesController
 * @description
 * @create 2025/5/16 02:25
 */
@RestController
@RequestMapping("/clothes")
public class ClothesController {
    private static final Logger logger = LoggerFactory.getLogger(ClothesController.class);
    @Autowired
    ClothesService clothesService;

    /**
     * 购买形体戏剧服装
     *
     * @param tClothesOrder
     * @param request
     * @return
     */
    @PostMapping("/preBuyClothes")
    public ApiResponse preBuyClothes(@RequestBody TClothesOrder tClothesOrder, HttpServletRequest request) {
        logger.info("preUpgrade::tClothesOrder = [{}]", tClothesOrder);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(tClothesOrder.getUserId()).orElseThrow(() -> new RuntimeException("用户id为空"));
            Optional.ofNullable(tClothesOrder.getAmount()).orElseThrow(() -> new RuntimeException("金额不能为空"));

            //要下一个会员升级订单的话要创建一个支付订单
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(clothesService.preBuyClothes(tClothesOrder, IpUtils.getIp(request)));

        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;


    }

    @PostMapping("/successToBuyClothes")
    public ApiResponse successToBuyClothes(@RequestBody TClothesOrder tClothesOrder) {
        logger.info("successToBuyClothes::tClothesOrder = [{}]", tClothesOrder);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
//默认没收到支付成功回调,但是收到了前端支付成功的回调
            Optional.ofNullable(tClothesOrder.getId()).orElseThrow(() -> new RuntimeException("id为空"));
            apiResponse.setData(clothesService.successToBuyClothes(tClothesOrder));
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }

        return apiResponse;
    }


}
