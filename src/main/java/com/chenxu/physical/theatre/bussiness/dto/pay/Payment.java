package com.chenxu.physical.theatre.bussiness.dto.pay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author mamingze
 * @version 1.0
 * @title Payment
 * @description
 * @create 2025/5/11 14:59
 */
@Data
@ToString
public class Payment {
    String appId;
    String timeStamp;
    String nonceStr;
    @JsonProperty("package")
    PackageObject packageObject;

}
