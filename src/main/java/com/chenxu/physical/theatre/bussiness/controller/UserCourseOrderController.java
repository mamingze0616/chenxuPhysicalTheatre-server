package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.database.constant.TCourseOrderStatus;
import com.chenxu.physical.theatre.database.constant.TUserType;
import com.chenxu.physical.theatre.database.domain.TCourseOrder;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.service.TCourseOrderService;
import com.chenxu.physical.theatre.database.service.TUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author mamingze
 * @version 1.0
 * @title UserCourseOrderController
 * @description
 * @create 2025/3/28 11:08
 */
@RestController
@RequestMapping("/order/course")
public class UserCourseOrderController {
    private static final Logger logger = LoggerFactory.getLogger(UserCourseOrderController.class);
    @Autowired
    TCourseOrderService courseOrderService;
    @Autowired
    TUserService tUserService;

    /**
     * 获取个人全部订单
     *
     * @param openid
     * @param tUser
     * @return
     */
    @PostMapping("/queryALLByUserId")
    public ApiResponse queryALLByUserId(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none") String openid, @RequestBody TUser tUser) {
        logger.info("queryByUserId::openid = [{}], tUser = [{}]", openid, tUser);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(tUser.getId()).ifPresentOrElse(userId -> {
                Optional.ofNullable(courseOrderService.list(new QueryWrapper<TCourseOrder>().eq("user_id", userId))).ifPresentOrElse(tCourseOrder -> {
                    apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                    apiResponse.setData(tCourseOrder);
                }, () -> {
                    throw new RuntimeException("此userId的数据为空");
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
     * 添加课程订单
     *
     * @param openid
     * @param courseOrder
     * @return
     */
    @PostMapping("add")
    public ApiResponse addCourseOrder(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none") String openid,
                                      @RequestBody TCourseOrder courseOrder) {
        logger.info("addCourseOrder::openid = [{}], courseOrder = [{}]", openid, courseOrder);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(courseOrder.getUserId()).ifPresentOrElse(userId -> {
                //根据courseOrder传过来的operatorId查找用户
                Optional.ofNullable(courseOrder.getOperatorId()).ifPresentOrElse(operatorId -> {
                    Optional.ofNullable(tUserService.getOne(new QueryWrapper<TUser>().eq("id", courseOrder.getOperatorId()))).ifPresentOrElse(tUser -> {
                        if (TUserType.ADMIN.equals(tUser.getType()) && openid.equals(tUser.getOpenid())) {
                            //是管理员,则增加课程订单
                            courseOrder.setCreateAt(LocalDateTime.now());
                            courseOrder.setStatus(TCourseOrderStatus.NORMAL);
                            if (courseOrderService.save(courseOrder)) {
                                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                                apiResponse.setData(courseOrder);
                            }
                        } else {
                            logger.info("非管理员,无法操作");
                            apiResponse.setErrorMsg("非管理员,无法操作");
                            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
                        }
                    }, () -> {
                        throw new RuntimeException("此openid的数据为空");
                    });
                }, () -> {
                    throw new RuntimeException("operatorId为空");
                });
            }, () -> {
                throw new RuntimeException("userId为空");
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg("添加失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }

    @PostMapping("update")
    public ApiResponse updateCourseOrder(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                         String openid, @RequestBody TCourseOrder courseOrder) {
        logger.info("updateCourseOrder::openid = [{}], courseOrder = [{}]", openid, courseOrder);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            //根据courseOrder传过来的id查找课程订单
            Optional.ofNullable(courseOrder.getId()).ifPresentOrElse(id -> {
                //存在则修改订单状态和数量
                Optional.ofNullable(courseOrderService.getById(id)).ifPresentOrElse(tCourseOrder -> {
                    tCourseOrder.setStatus(courseOrder.getStatus());
                    tCourseOrder.setCourseNumber(courseOrder.getCourseNumber());
                    if (courseOrderService.updateById(tCourseOrder)) {
                        apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                        apiResponse.setData(tCourseOrder);
                    } else {
                        apiResponse.setErrorMsg("修改失败");
                        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
                    }
                }, () -> {
                    throw new RuntimeException("此id的数据为空");
                });
            }, () -> {
                throw new RuntimeException("id为空");
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg("修改失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }


}
