package com.chenxu.physical.theatre.database.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenxu.physical.theatre.database.domain.TUser;

/**
 * @author mamingze
 * @description 针对表【T_USER(user info)】的数据库操作Service
 * @createDate 2025-03-04 16:41:54
 */
public interface TUserService extends IService<TUser> {
    PageDTO<TUser> selectPageTUserWithAppointmentInfoList(PageDTO<TUser> page, TUser user);

}
