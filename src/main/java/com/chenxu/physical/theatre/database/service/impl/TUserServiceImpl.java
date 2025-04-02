package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import com.chenxu.physical.theatre.database.domain.TUser;
import com.chenxu.physical.theatre.database.mapper.TUserMapper;
import com.chenxu.physical.theatre.database.service.TAppointmentInfoService;
import com.chenxu.physical.theatre.database.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author mamingze
 * @description 针对表【T_USER(user info)】的数据库操作Service实现
 * @createDate 2025-03-04 16:41:54
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser>
        implements TUserService {
    @Autowired
    private TAppointmentInfoService tAppointmentInfoService;

    @Override
    public PageDTO<TUser> selectPageTUserWithAppointmentInfoList(PageDTO<TUser> page, TUser user) {
        QueryWrapper<TUser> queryWrapper = new QueryWrapper<>();
        Optional.ofNullable(user.getId()).ifPresent(id -> queryWrapper.like("id", id));
        Optional.ofNullable(user.getNickname()).ifPresent(nickname -> queryWrapper.like("nickname", nickname));
        // 分页查询
        PageDTO<TUser> tUserPageDTODTO = page(page, queryWrapper);
        List<TUser> tUserList = Optional.ofNullable(tUserPageDTODTO.getRecords()).orElse(new ArrayList<>());

        List<Integer> idList = tUserList.stream().map(TUser::getId).collect(Collectors.toList());
        if (idList.isEmpty()) {
            return tUserPageDTODTO;
        }
        Map<Integer, List<TAppointmentInfo>> tAppointmentInfoMap = tAppointmentInfoService
                .list(new QueryWrapper<TAppointmentInfo>().in("user_id", idList)).stream()
                .collect(Collectors.groupingBy(TAppointmentInfo::getUserId));
        tUserList.forEach(tUser -> {
            if (tAppointmentInfoMap.containsKey(tUser.getId())) {
                tUser.setAppointmentInfos(tAppointmentInfoMap.get(tUser.getId()));
            }
        });
        tUserPageDTODTO.setRecords(tUserList);

        return tUserPageDTODTO;
    }
}




