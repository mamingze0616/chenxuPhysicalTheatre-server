package com.chenxu.physical.theatre.bussiness.dto.pay;

import lombok.Data;
import lombok.ToString;

/**
 * @author mamingze
 * @version 1.0
 * @title ApiUnifiedOrderRequest
 * @description
 * @create 2025/5/11 05:29
 */
@Data
@ToString
public class ApiUnifiedOrderRequest {
    String body;
    String outTradeNo;
    int totalFee;
}
