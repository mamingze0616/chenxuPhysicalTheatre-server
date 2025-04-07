package com.chenxu.physical.theatre.bussiness.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author mamingze
 * @version 1.0
 * @title PhoneInfo
 * @description
 * @create 2025/4/7 21:21
 */
@Data
@ToString
public class PhoneInfo {
    private String phoneNumber;
    private String purePhoneNumber;
    private String countryCode;
}
