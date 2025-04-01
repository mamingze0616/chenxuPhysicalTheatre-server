package com.chenxu.physical.theatre.bussiness.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author mamingze
 * @version 1.0
 * @title ApiRequestPageDto
 * @description
 * @create 2025/4/1 11:05
 */
@Data
public class ApiRequestPageDto {
    @TableField(exist = false)
    Integer total;
    @TableField(exist = false)
    Integer size;
    @TableField(exist = false)
    Integer current;

}
