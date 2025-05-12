package com.chenxu.physical.theatre.bussiness.dto.pay;

import lombok.Data;
import lombok.ToString;

/**
 * @author mamingze
 * @version 1.0
 * @title PayUnifiedOrderResponse
 * @description
 * @create 2025/5/11 14:55
 */
@Data
@ToString
public class PayUnifiedOrderResponse {
    int errcode;
    String errmsg;
    Respdata respdata;

}
