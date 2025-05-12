package com.chenxu.physical.theatre.bussiness.dto;

//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;
import lombok.ToString;

/**
 * @author mamingze
 * @version 1.0
 * @title ApiPayCallbackRequest
 * @description
 * @create 2025/5/12 22:42
 */
@Data
@ToString
//@JacksonXmlRootElement(localName = "xml")
public class ApiPayCallbackRequest {
    String returnCode;
    String appid;
    String mchId;
    String subAppid;
    String subMchId;
    String nonceStr;
    String resultCode;
    String openid;
    String isSubscribe;
    String subOpenid;
    String subIsSubscribe;
    String tradeType;
    String bankType;
    String totalFee;
    String feeType;
    String cashFee;
    String transactionId;
    String outTradeNo;
    String timeEnd;
}
