package com.chenxu.physical.theatre.bussiness.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.dto.ApiPayCallbackRequest;
import com.chenxu.physical.theatre.bussiness.dto.pay.PayUnifiedOrderResponse;
import com.chenxu.physical.theatre.database.constant.TPayOrderStatus;
import com.chenxu.physical.theatre.database.constant.TPayOrderType;
import com.chenxu.physical.theatre.database.domain.*;
import com.chenxu.physical.theatre.database.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author mamingze
 * @version 1.0
 * @title PayService
 * @description
 * @create 2025/5/11 02:33
 */
@Service
public class PayService {
    private static final int WEIXIN_RESPONSE_CODE_SUCCESS = 0;
    private static final String WEIXIN_RESPONSE_RETURN_CODE_SUCCESS = "SUCCESS";
    private static final String WEIXIN_RESPONSE_RESULT_CODE_SUCCESS = "SUCCESS";

    private static final Logger logger = LoggerFactory.getLogger(PayService.class);
    @Value("${pay.unifiedOrder.url}")
    private String unifiedOrderUrl;
    @Value("${pay.orderquery.url}")
    private String queryOrderUrl;
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
    @Autowired
    UserService userService;


    @Autowired
    TUserOrderService userOrderService;
    @Autowired
    TClothesOrderService tClothesOrderService;
    @Autowired
    TCourseOrderService tCourseOrderService;
    @Autowired
    TActivityBookedInfoService tActivityBookedInfoService;
    @Autowired
    CourseOrderSplitService courseOrderSplitService;

    public boolean finishedPayOrder(ApiPayCallbackRequest apiPayCallbackRequest) {
        try {
            TPayOrder tPayOrder = payOrderService.getOne(new QueryWrapper<TPayOrder>().eq("out_trade_no", apiPayCallbackRequest.getOutTradeNo()));

            if (tPayOrder != null) {
                tPayOrder.setStatus(TPayOrderStatus.SUCCESS);
                tPayOrder.setResultCode(apiPayCallbackRequest.getResultCode());
                tPayOrder.setTimeEnd(apiPayCallbackRequest.getTimeEnd());
                tPayOrder.setTransactionId(apiPayCallbackRequest.getTransactionId());
                tPayOrder.setPayJson(apiPayCallbackRequest);
                //按照_分割
                String[] split = tPayOrder.getOutTradeNo().split("_");
                if (TPayOrderType.MEMBERSHIP.getCode().equals(Integer.parseInt(split[1]))) {
//                    tUserService.lambdaUpdate().set(TUser::getType, TUserType.MEMBER.getCode())
//                            .eq(TUser::getOpenid, tPayOrder.getOpenid())
//                            .update();
                } else if (TPayOrderType.COURSE.getCode().equals(Integer.parseInt(split[1]))) {
                    TCourseOrder courseOrder = courseOrderSplitService.payCallback(Integer.valueOf(split[2]));
                    userService.updateEffectiveCourseCount(courseOrder.getUserId());
                } else if (TPayOrderType.ACTIVITY.getCode().equals(Integer.parseInt(split[1]))) {

                }
                return payOrderService.updateById(tPayOrder);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return false;


    }


    public TPayOrder preCreateMembershipPayOrder(TUserOrder tUserOrder, String body) {
        TPayOrder tPayOrder = new TPayOrder();
        try {
            String openid = userService.getById(tUserOrder.getUserId()).getOpenid();
            tPayOrder.setType(TPayOrderType.MEMBERSHIP);
            tPayOrder.setOpenid(openid);
            tPayOrder.setBody(body);
            tPayOrder.setStatus(TPayOrderStatus.UNPAID);
            tPayOrder.setTotalFee(tUserOrder.getAmount());
            payOrderService.save(tPayOrder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return tPayOrder;
    }

    public TPayOrder preBooked(TActivityBookedInfo tActivityBookedInfo, String body) {
        TPayOrder tPayOrder = new TPayOrder();
        try {
            String openid = userService.getById(tActivityBookedInfo.getUserId()).getOpenid();
            tPayOrder.setType(TPayOrderType.MEMBERSHIP);
            tPayOrder.setOpenid(openid);
            tPayOrder.setBody(body);
            tPayOrder.setStatus(TPayOrderStatus.UNPAID);
            tPayOrder.setTotalFee(tActivityBookedInfo.getAmount());
            payOrderService.save(tPayOrder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return tPayOrder;
    }

    public TPayOrder preCreateClothesPayOrder(TClothesOrder tClothesOrder, String body) {
        TPayOrder tPayOrder = new TPayOrder();
        try {
            String openid = userService.getById(tClothesOrder.getUserId()).getOpenid();
            tPayOrder.setType(TPayOrderType.CLOTHES);
            tPayOrder.setOpenid(openid);
            tPayOrder.setBody(body);
            tPayOrder.setStatus(TPayOrderStatus.UNPAID);
            tPayOrder.setTotalFee(tClothesOrder.getAmount());
            payOrderService.save(tPayOrder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return tPayOrder;
    }

    public TPayOrder preCreateCourseOrder(TCourseOrder tCourseOrder, String body) {
        TPayOrder tPayOrder = new TPayOrder();
        try {
            String openid = userService.getById(tCourseOrder.getUserId()).getOpenid();
            tPayOrder.setType(TPayOrderType.COURSE);
            tPayOrder.setOpenid(openid);
            tPayOrder.setBody(body);
            tPayOrder.setStatus(TPayOrderStatus.UNPAID);
            tPayOrder.setTotalFee(tCourseOrder.getAmount());
            payOrderService.save(tPayOrder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return tPayOrder;
    }

    public TPayOrder bindOutTradeNo(TPayOrder tPayOrder, int outTradeNo, TPayOrderType type) {
        //
        StringBuilder sb = new StringBuilder();
        sb.append(tPayOrder.getId()).append("_").append(type.getCode()).append("_").append(outTradeNo);
        tPayOrder.setOutTradeNo(sb.toString());
        tPayOrder.setType(type);
        payOrderService.updateById(tPayOrder);
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
    public TPayOrder unifiedOrder(TPayOrder tPayOrder, int outTradeNo, TPayOrderType type, String spbillCreateIp) {
        try {
            //绑定关联ip后组成的OutTradeNo
            bindOutTradeNo(tPayOrder, outTradeNo, type);
            PayUnifiedOrderResponse payUnifiedOrderResponse = unifiedOrder(tPayOrder.getOpenid(), tPayOrder.getBody(), tPayOrder.getOutTradeNo(), tPayOrder.getTotalFee(), spbillCreateIp);
            //将预订单的返回结果存储
            tPayOrder.setPreJson(payUnifiedOrderResponse);
            tPayOrder.setTimeStamp(payUnifiedOrderResponse.getRespdata().getPayment().getTimeStamp());
            tPayOrder.setResultCode(payUnifiedOrderResponse.getRespdata().getResultCode());
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


            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map> entity = new HttpEntity<>(requestBody, headers);
            String responseText = restTemplate.postForObject(unifiedOrderUrl, entity, String.class);
            logger.info("接口返回:[{}]", responseText);
            // 2. 然后手动转换为 PhoneResponse
            ObjectMapper mapper = new ObjectMapper();
            PayUnifiedOrderResponse payUnifiedOrderResponse = mapper.readValue(responseText, PayUnifiedOrderResponse.class);
            return payUnifiedOrderResponse;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("统一下单失败:" + e.getMessage());
            throw new RuntimeException("统一下单失败");
        }

    }


    //获取该用户的所有类型的订单
    public List<TPayOrder> getPayOrdersByUserId(TUser tUser) {
        List<TPayOrder> tPayOrders = new ArrayList<>();
        try {
            String openid = userService.getById(tUser.getId()).getOpenid();
            tPayOrders = payOrderService.list(new QueryWrapper<TPayOrder>().eq("openid", openid));
            tPayOrders.forEach(tPayOrder -> {
                //
                String getOutTradeNo = tPayOrder.getOutTradeNo();
                //按照_分割
                String[] split = getOutTradeNo.split("_");
                if (TPayOrderType.MEMBERSHIP.getCode().equals(Integer.parseInt(split[1]))) {
                    tPayOrder.setUserOrder(userOrderService.getById(Integer.parseInt(split[2])));
                } else if (TPayOrderType.CLOTHES.getCode().equals(Integer.parseInt(split[1]))) {
                    tPayOrder.setClothesOrder(tClothesOrderService.getById(Integer.parseInt(split[2])));
                } else if (TPayOrderType.COURSE.getCode().equals(Integer.parseInt(split[1]))) {
                    tPayOrder.setCourseOrder(tCourseOrderService.getById(Integer.parseInt(split[2])));
                } else if (TPayOrderType.ACTIVITY.getCode().equals(Integer.parseInt(split[1]))) {
                    tPayOrder.setActivityBookedInfo(tActivityBookedInfoService.getById(Integer.parseInt(split[2])));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取该用户的所有类型的订单失败:" + e.getMessage());
            throw new RuntimeException("获取该用户的所有类型的订单失败");
        }
        return tPayOrders;
    }

}
