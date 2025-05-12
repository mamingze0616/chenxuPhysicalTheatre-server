package com.chenxu.physical.theatre.bussiness.dto.pay;

import lombok.Data;
import lombok.ToString;

/**
 * @author mamingze
 * @version 1.0
 * @title PackageObject
 * @description
 * @create 2025/5/11 15:02
 */
@Data
@ToString
public class PackageObject {
    String prepay_id;
    String signType;
    String paySign;


}
