package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxu.physical.theatre.database.domain.TCourseOrder;
import com.chenxu.physical.theatre.database.mapper.TCourseOrderMapper;
import com.chenxu.physical.theatre.database.service.TCourseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author mamingze
 * @description 针对表【T_COURSE_ORDER(课程订单信息)】的数据库操作Service实现
 * @createDate 2025-03-28 17:19:58
 */
@Service
public class TCourseOrderServiceImpl extends ServiceImpl<TCourseOrderMapper, TCourseOrder> implements TCourseOrderService {
    private static final Logger logger = LoggerFactory.getLogger(TCourseOrderServiceImpl.class);
}




