package com.chenxu.physical.theatre.bussiness.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author mamingze
 * @version 1.0
 * @title ApiPayCallbackResponse
 * @description
 * @create 2025/5/13 00:00
 */
@Data
@ToString
//@JacksonXmlRootElement(localName = "xml")
public class ApiPayCallbackResponse {
    String returnCode;
    String returnMsg;
}
