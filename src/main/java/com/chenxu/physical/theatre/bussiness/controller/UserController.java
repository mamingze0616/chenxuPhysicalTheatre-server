package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.database.constant.TUserStatus;
import com.chenxu.physical.theatre.database.constant.TUserType;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.service.TUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

/**
 * @author mamingze
 * @version 1.0
 * @title UserController
 * @description
 * @create 2025/3/4 16:51
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    TUserService tUserService;

    @PostMapping("/login")
    public ApiResponse login(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                             String openid) {
        logger.info("login::openid = [{}]", openid);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        apiResponse.setErrorMsg("登陆失败");
        // 先查询一次是不是包含该用户
        try {
            Optional.ofNullable(openid).orElseThrow(() -> new RuntimeException("openid为空"));
            Optional<TUser> tUserOptions = Optional.ofNullable(tUserService.
                    getOne(new QueryWrapper<TUser>().eq("openid", openid), false));
            tUserOptions.ifPresentOrElse(tUser -> {
                //更新登陆时间
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(tUser);
                apiResponse.setErrorMsg("登陆成功");
            }, () -> {
                TUser tUser = new TUser();
                tUser.setOpenid(openid);
                tUser.setStatus(TUserStatus.NEW_ADDED.getCode());
                tUser.setType(TUserType.USER.getCode());
                tUser.setNickname(openid.substring(0, 6));
                tUser.setAvatar("https://tdesign.gtimg.com/mobile/demos/avatar1.png");
                //时区有问题暂时不修复
                TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
                tUser.setCreatedAt(new Date());
                tUserService.save(tUser);
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(tUser);
                apiResponse.setErrorMsg("登陆成功");
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg("登陆失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }

    @PostMapping("/update")
    public ApiResponse
    updateNicknameAndAvatar(@RequestBody TUser user,
                            @RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                            String openid) {
        logger.info("updateNicknameAndAvatar::user = [{}], openid = [{}]", user, openid);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        apiResponse.setErrorMsg("更新失败");
        try {
            //查询出原先的数据
            TUser queryUser = tUserService.getById(user.getId());
            if (queryUser == null) {
                queryUser = tUserService.getOne(new LambdaQueryWrapper<>(TUser.class)
                        .eq(TUser::getOpenid, openid));
            }
            //更新指定字段
            queryUser.setNickname(user.getNickname());
            queryUser.setAvatar(user.getAvatar());
            queryUser.setPhone(user.getPhone());
            if (tUserService.updateById(queryUser)) {
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setErrorMsg("更新成功");
                apiResponse.setData(queryUser);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return apiResponse;
    }

    /**
     * 查询所有type=2是user的用户
     *
     * @return
     */
    @PostMapping("/getAllUser")
    public ApiResponse getAllUser() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        apiResponse.setErrorMsg("获取失败");
        try {
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(tUserService.list(new QueryWrapper<TUser>().eq("type", 2)));
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg("获取失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);

        }
        return apiResponse;
    }

    @PostMapping("/getUserById")
    public ApiResponse getUserById(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                   String openid, @RequestBody TUser user) {
        logger.info("getUserById::user = [{}], openid = [{}]", user, openid);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(user.getId()).ifPresentOrElse(id -> {
                Optional.ofNullable(tUserService.getById(id)).ifPresentOrElse(tUser -> {
                    apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                    apiResponse.setData(tUser);
                }, () -> new RuntimeException("此id的数据为空"));
            }, () -> new RuntimeException("id为空"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }
}
