package com.chenxu.physical.theatre.database.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenxu.physical.theatre.database.domain.TCourse;

/**
 * @author mamingze
 * @description 针对表【T_COURSE(课程信息表)】的数据库操作Service
 * @createDate 2025-03-27 10:14:18
 */
public interface TCourseService extends IService<TCourse> {
    PageDTO<TCourse> selectPageTCourseList(PageDTO<TCourse> page, TCourse course);
}
