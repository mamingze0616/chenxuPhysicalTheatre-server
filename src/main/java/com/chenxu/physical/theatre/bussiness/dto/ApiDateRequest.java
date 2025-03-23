package com.chenxu.physical.theatre.bussiness.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;


/**
 * @author mamingze
 * @version 1.0
 * @title ApiDateRequest
 * @description
 * @create 2025/3/23 13:52
 */
@Data
public class ApiDateRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
