package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxu.physical.theatre.database.domain.TCourseOrderSpilt;
import com.chenxu.physical.theatre.database.service.TCourseOrderSpiltService;
import com.chenxu.physical.theatre.database.mapper.TCourseOrderSpiltMapper;
import org.springframework.stereotype.Service;

/**
* @author mamingze
* @description 针对表【T_COURSE_ORDER_SPILT(课程订单拆分表,将每个订单拆分成一节一节课程来实现过期未用完的情况)】的数据库操作Service实现
* @createDate 2025-05-19 00:08:47
*/
@Service
public class TCourseOrderSpiltServiceImpl extends ServiceImpl<TCourseOrderSpiltMapper, TCourseOrderSpilt>
    implements TCourseOrderSpiltService{

}




