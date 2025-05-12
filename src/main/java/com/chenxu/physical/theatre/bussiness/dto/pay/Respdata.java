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
    String return_code;
    String return_msg;
    String appid;
    String mch_id;
    String sub_appid;
    String sub_mch_id;
    String nonce_str;
    String sign;
    String result_code;
    String trade_type;
    String prepay_id;
    Payment payment;
}
