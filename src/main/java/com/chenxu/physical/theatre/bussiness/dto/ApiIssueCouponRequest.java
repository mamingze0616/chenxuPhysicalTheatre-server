package com.chenxu.physical.theatre.bussiness.dto;

import lombok.Data;

/**
 * @author mamingze
 * @version 1.0
 * @title ApiIssueCouponRequest
 * @description
 * @create 2025/5/9 13:14
 */
@Data
public class ApiIssueCouponRequest {
    String userIds;
    Integer couponId;
}
