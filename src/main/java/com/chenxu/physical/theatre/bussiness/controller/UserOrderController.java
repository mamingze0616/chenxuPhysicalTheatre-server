package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.database.domain.TUserOrder;
import com.chenxu.physical.theatre.database.service.TUserOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author mamingze
 * @version 1.0
 * @title UserOrderController
 * @description
 * @create 2025/5/13 15:44
 */
@RestController
@RequestMapping("/order")
public class UserOrderController {
    private static final Logger logger = LoggerFactory.getLogger(UserOrderController.class);
    @Autowired
    TUserOrderService userOrderService;

    /**
     * 根据userId获取用户订单
     *
     * @param tUserOrder
     * @return
     */
    @RequestMapping("/getUserOrderList")
    public ApiResponse getUserOrderList(@RequestBody TUserOrder tUserOrder) {
        logger.info("getUserOrderList::tUserOrder = [{}]", tUserOrder);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(tUserOrder.getUserId())
                    .orElseThrow(() -> new RuntimeException("userId为空"));
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            //按照用户id查询用户订单
            apiResponse.setData(userOrderService.list(new QueryWrapper<TUserOrder>()
                    .eq("user_id", tUserOrder.getUserId())));
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
            apiResponse.setErrorMsg(e.getMessage());
        }
        return apiResponse;
    }
}
