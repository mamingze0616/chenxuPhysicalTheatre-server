package com.chenxu.physical.theatre.bussiness.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author mamingze
 * @version 1.0
 * @title PhoneResponse
 * @description
 * @create 2025/4/7 20:55
 */
@Data
@ToString
public class PhoneResponse {
    private int errcode;
    private String errmsg;
    private PhoneInfo phone_info;


}
