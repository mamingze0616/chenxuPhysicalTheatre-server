package com.chenxu.physical.theatre.bussiness.service;

import com.chenxu.physical.theatre.bussiness.dto.pay.PayUnifiedOrderResponse;
import com.chenxu.physical.theatre.database.constant.TPayOrderType;
import com.chenxu.physical.theatre.database.domain.TPayOrder;
import com.chenxu.physical.theatre.database.service.TPayOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mamingze
 * @version 1.0
 * @title PayService
 * @description
 * @create 2025/5/11 02:33
 */
@Service
public class PayService {
    private static final Logger logger = LoggerFactory.getLogger(PayService.class);
    @Value("${pay.unifiedOrder.url}")
    private String unifiedOrderUrl;
    @Value("${wx.env}")
    private String env;
    @Value("${wx.mch}")
    private String mch;
    @Value("${pay.unifiedOrder.callback.service}")
    private String service;
    @Value("${pay.unifiedOrder.callback.path}")
    private String path;

    @Autowired
    TPayOrderService payOrderService;

    @Autowired
    RestTemplate restTemplate;


    public TPayOrder preCreatePayOrder(String openid, String body, Integer totalFee) {
        TPayOrder tPayOrder = new TPayOrder();
        tPayOrder.setOpenid(openid);
        tPayOrder.setBody(body);
        tPayOrder.setTotalFee(totalFee);
        payOrderService.save(tPayOrder);
        return tPayOrder;
    }

    public TPayOrder bindOutTradeNo(TPayOrder tPayOrder, int outTradeNo, TPayOrderType type) {
        //
        StringBuilder sb = new StringBuilder();
        sb.append(tPayOrder.getId()).append("_").append(type.getCode()).append("_").append(outTradeNo);
        tPayOrder.setOutTradeNo(sb.toString());
        tPayOrder.setType(type);
        payOrderService.save(tPayOrder);
        return tPayOrder;
    }

    /**
     * 预支付订单
     *
     * @param tPayOrder
     * @param outTradeNo
     * @param type
     * @param spbillCreateIp
     * @return
     */
    public TPayOrder unifiedOrder(TPayOrder tPayOrder, int outTradeNo,
                                  TPayOrderType type,
                                  String spbillCreateIp) {
        try {
            //绑定关联ip后组成的OutTradeNo
            bindOutTradeNo(tPayOrder, outTradeNo, type);
            PayUnifiedOrderResponse payUnifiedOrderResponse = unifiedOrder(tPayOrder.getOpenid(),
                    tPayOrder.getBody(), tPayOrder.getOutTradeNo(),
                    tPayOrder.getTotalFee(), spbillCreateIp);
            //将预订单的返回结果存储
            tPayOrder.setPreJson(payUnifiedOrderResponse);
            payOrderService.updateById(tPayOrder);
            return tPayOrder;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("统一下单失败:" + e.getMessage());
            throw new RuntimeException("统一下单失败");
        }

    }


    /**
     * @param openid
     * @param body
     * @param outTradeNo 内部订单号
     * @param totalFee
     * @return
     */
    public PayUnifiedOrderResponse unifiedOrder(String openid, String body, String outTradeNo, int totalFee, String spbillCreateIp) {

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("body", body);
            requestBody.put("openid", openid);
            requestBody.put("out_trade_no", outTradeNo);
            requestBody.put("spbill_create_ip", spbillCreateIp);
            requestBody.put("env_id", env);
            requestBody.put("sub_mch_id", mch);
            requestBody.put("total_fee", totalFee);
            requestBody.put("callback_type", 2);//2是云托管callback类型
            Map<String, String> container = new HashMap<>();
            container.put("service", service);
            container.put("path", path);
            requestBody.put("container", container);
            logger.info("接口请求:[{}]", requestBody);
            String responseText = restTemplate.postForObject(unifiedOrderUrl, requestBody, String.class);
            // 2. 然后手动转换为 PhoneResponse
            ObjectMapper mapper = new ObjectMapper();

            PayUnifiedOrderResponse payUnifiedOrderResponse = mapper.readValue(responseText,
                    PayUnifiedOrderResponse.class);
            logger.info("接口返回:[{}]", payUnifiedOrderResponse);
            return payUnifiedOrderResponse;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("统一下单失败:" + e.getMessage());
            throw new RuntimeException("统一下单失败");
        }

    }

}
