package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.service.TUserService;
import com.chenxu.physical.theatre.database.mapper.TUserMapper;
import org.springframework.stereotype.Service;

/**
* @author mamingze
* @description 针对表【T_USER(user info)】的数据库操作Service实现
* @createDate 2025-03-04 16:41:54
*/
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser>
    implements TUserService{

}




