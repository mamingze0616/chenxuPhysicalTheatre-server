package com.chenxu.physical.theatre.bussiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.constant.Constant;
import com.chenxu.physical.theatre.bussiness.dto.ApiResponse;
import com.chenxu.physical.theatre.bussiness.service.UserService;
import com.chenxu.physical.theatre.database.constant.TUserStatus;
import com.chenxu.physical.theatre.database.constant.TUserType;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.service.TUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    UserService userService;

    @PostMapping("/update")
    public ApiResponse updateNicknameAndAvatar(@RequestBody TUser user) {
        logger.info("updateNicknameAndAvatar::user = [{}]", user);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        apiResponse.setErrorMsg("更新失败");
        try {
            //查询出原先的数据
            Optional.ofNullable(user.getId()).ifPresentOrElse(id -> {
                Optional.ofNullable(tUserService.getById(id)).ifPresentOrElse(tUser -> {
                    tUser.setNickname(user.getNickname());
                    tUser.setAvatar(user.getAvatar());
                    tUser.setStatus(TUserStatus.NORMAL.getCode());
                    if (tUserService.updateById(tUser)) {
                        apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                        apiResponse.setErrorMsg("更新成功");
                        apiResponse.setData(userService.getUserCardInfo(tUser));
                    }
                }, () -> {
                    throw new RuntimeException("此id的数据为空");
                });
            }, () -> {
                throw new RuntimeException("id为空");
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return apiResponse;
    }

    /**
     * 查询所有user的用户
     *
     * @return
     */
    @PostMapping("/getAllUser")
    public ApiResponse getAllUser() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
            apiResponse.setData(tUserService.list());
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg("获取失败");
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);

        }
        return apiResponse;
    }

    @PostMapping("/getUserById")
    public ApiResponse getUserById(@RequestBody TUser user) {
        logger.info("getUserById::user = [{}]", user);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(user.getId()).orElseThrow(() -> new RuntimeException("id为空"));
            Optional.ofNullable(tUserService.getById(user.getId())).ifPresentOrElse(tUser -> {
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(userService.getUserCardInfo(tUser));
            }, () -> {
                throw new RuntimeException("此id的数据为空");
            });

        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }

    @PostMapping("/changeTypeToAdmin")
    public ApiResponse changeTypeToAdmin(@RequestBody TUser user) {
        logger.info("changeTypeToAdmin::user = [{}]", user);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            Optional.ofNullable(user.getId()).orElseThrow(() -> new RuntimeException("id为空"));
            //管理员数量限制
            if (tUserService.count(new QueryWrapper<TUser>().eq("type", TUserType.ADMIN.getCode())) >= 3) {
                apiResponse.setErrorMsg("管理员数量已达上限");
                apiResponse.setCode(Constant.APIRESPONSE_FAIL);
                return apiResponse;
            }

            Optional.ofNullable(tUserService.getById(user.getId())).ifPresentOrElse(tUser -> {

                tUser.setType(TUserType.ADMIN);
                if (tUserService.updateById(tUser)) {
                    apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                    apiResponse.setErrorMsg("更新成功");
                } else {
                    apiResponse.setErrorMsg("更新失败");
                    apiResponse.setCode(Constant.APIRESPONSE_FAIL);
                }
            }, () -> {
                throw new RuntimeException("此id的数据为空");
            });

        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }

    //按照名称或者手机号搜索所有用户
    @PostMapping("/searchUser")
    public ApiResponse searchUser(@RequestParam String search, @RequestParam int type) {
        logger.info("searchUser::search = [{}],type=[{}]", search, type);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            QueryWrapper<TUser> queryWrapper = new QueryWrapper();
            queryWrapper.eq("type", type).and(!StringUtils.isEmpty(search), wrapper ->
                    wrapper.like("nickname", search).or().like("phone", search));
            apiResponse.setData(tUserService.list(queryWrapper));
            apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;
    }

    /**
     * 注册登录接口,还没做状态判断
     *
     * @param openid
     * @param code
     * @return
     */
    @PostMapping("/registerOrLogin")
    public ApiResponse registerOrLogin(@RequestHeader(value = "X-WX-OPENID", required = false, defaultValue = "none")
                                       String openid, @RequestParam String code) {
        logger.info("registerOrLogin::code = [{}]", code);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        try {
            String phoneNumber = userService.getUserPhoneNumber(code);
            Optional.ofNullable(tUserService.getOne(new QueryWrapper<TUser>().eq("phone", phoneNumber))).ifPresentOrElse(tUser -> {
                userService.loginUpdateUserInfo(tUser);
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(tUser);
                apiResponse.setErrorMsg("登陆成功");
            }, () -> {
                TUser tUser = new TUser();
                tUser.setOpenid(openid);
                tUser.setStatus(TUserStatus.NEW_ADDED.getCode());
                tUser.setPhone(phoneNumber);
                tUser.setType(TUserType.NON_MEMBER);
                tUser.setNickname(openid.substring(22));
                tUser.setAvatar("https://tdesign.gtimg.com/mobile/demos/avatar1.png");
                logger.info("registerOrLogin::tUser = [{}]", tUser);
                tUserService.save(tUser);
                apiResponse.setCode(Constant.APIRESPONSE_SUCCESS);
                apiResponse.setData(tUser);
                apiResponse.setErrorMsg("登陆成功");
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            apiResponse.setErrorMsg(e.getMessage());
            apiResponse.setCode(Constant.APIRESPONSE_FAIL);
        }
        return apiResponse;

    }


}
