package com.chenxu.physical.theatre.bussiness.dto.achievement;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author mamingze
 * @version 1.0
 * @title ApiSkillTypeModel
 * @description 关联picker需要的item
 * @create 2025/6/5 23:28
 */
@Data
@ToString
public class ApiSkillTypeModel {
    String label;
    String value;
    List<ApiAchievementModel> children;
}
