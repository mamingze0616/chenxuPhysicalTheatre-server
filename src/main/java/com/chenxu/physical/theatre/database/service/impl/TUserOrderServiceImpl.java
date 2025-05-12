package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxu.physical.theatre.database.domain.TUserOrder;
import com.chenxu.physical.theatre.database.service.TUserOrderService;
import com.chenxu.physical.theatre.database.mapper.TUserOrderMapper;
import org.springframework.stereotype.Service;

/**
* @author mamingze
* @description 针对表【T_USER_ORDER(用户升级成会员的订单)】的数据库操作Service实现
* @createDate 2025-05-12 15:12:51
*/
@Service
public class TUserOrderServiceImpl extends ServiceImpl<TUserOrderMapper, TUserOrder>
    implements TUserOrderService{

}




