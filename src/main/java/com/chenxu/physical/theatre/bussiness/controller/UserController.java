package com.chenxu.physical.theatre.bussiness.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

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
        logger.info("com.chenxu.physical.theatre.bussiness.controller-->login::openid = [{}]", openid);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        apiResponse.setErrorMsg("登陆失败");
        // 先查询一次是不是包含该用户
        try {
            Optional.ofNullable(openid).orElseThrow(() -> new RuntimeException("openid为空"));
            Optional<TUser> tUserOptions = Optional.ofNullable(tUserService.
                    getOne(new QueryWrapper<TUser>().eq("openid", openid), false));
            tUserOptions.ifPresentOrElse(tUser -> {
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(tUser);
                apiResponse.setErrorMsg("登陆成功");
            }, () -> {
                TUser tUser = new TUser();
                tUser.setOpenid(openid);
                tUser.setStatus(TUserStatus.NORMAL.getCode());
                tUser.setType(TUserType.USER.getCode());
                tUser.setNickname(openid.substring(0, 6));
                tUser.setAvatar("https://tdesign.gtimg.com/mobile/demos/avatar1.png");
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
}
