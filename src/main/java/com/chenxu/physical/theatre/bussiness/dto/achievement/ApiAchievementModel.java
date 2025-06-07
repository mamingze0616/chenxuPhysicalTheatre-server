package com.chenxu.physical.theatre.bussiness.dto.achievement;

import com.chenxu.physical.theatre.database.domain.TAppointmentInfo;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author mamingze
 * @version 1.0
 * @title ApiAchievementModel
 * @description
 * @create 2025/6/5 23:33
 */
@Data
@ToString
public class ApiAchievementModel {
    String label;
    String value;
    List<TAppointmentInfo> appointmentInfos;
}
