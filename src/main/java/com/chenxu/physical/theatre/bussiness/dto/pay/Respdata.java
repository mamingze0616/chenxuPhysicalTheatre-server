package com.chenxu.physical.theatre.bussiness.dto.pay;

import lombok.Data;
import lombok.ToString;

/**
 * @author mamingze
 * @version 1.0
 * @title Respdata
 * @description
 * @create 2025/5/11 14:56
 */
@Data
@ToString
public class Respdata {
    String returnCode;
    String returnMsg;
    String appid;
    String mchId;
    String subAppid;
    String subMchId;
    String nonceStr;
    String sign;
    String resultCode;
    String tradeType;
    String prepayId;
    Payment payment;
}
