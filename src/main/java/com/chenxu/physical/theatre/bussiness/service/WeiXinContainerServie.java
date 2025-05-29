package com.chenxu.physical.theatre.bussiness.service;

import com.chenxu.physical.theatre.bussiness.dto.PhoneResponse;
import com.chenxu.physical.theatre.bussiness.dto.UploadFile.BeforeUploadFileResponse;
import com.chenxu.physical.theatre.bussiness.dto.pay.PayUnifiedOrderResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mamingze
 * @version 1.0
 * @title WeiXinContainerServie
 * @description 外部微信容器服务调用开放接口服务
 * @create 2025/5/28 16:59
 */
@Service
public class WeiXinContainerServie {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinContainerServie.class);

    @Value("${wx.url.getPhoneNumber}")
    private String getPhoneNumber;

    @Value("${pay.unifiedOrder.url}")
    private String unifiedOrderUrl;

    @Value("${wx.url.beforeUploadFile}")
    private String beforeUploadFileUrl;

    @Value("${wx.env}")
    private String env;
    @Value("${wx.mch}")
    private String mch;
    @Value("${pay.unifiedOrder.callback.service}")
    private String service;
    @Value("${pay.unifiedOrder.callback.path}")
    private String path;

    /**
     * @param code
     * @return
     * @throws JsonProcessingException
     */
    public PhoneResponse getUserPhoneNumber(String code) {

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("code", code);
            String responseText = post(requestBody, getPhoneNumber);
            logger.info("接口返回:[{}]", responseText);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responseText, PhoneResponse.class);
        } catch (JsonProcessingException e) {
            logger.error("获取手机号失败:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 预下单
     *
     * @param openid
     * @param body
     * @param outTradeNo
     * @param totalFee
     * @param spbillCreateIp
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
            String responseText = post(requestBody, unifiedOrderUrl);
            logger.info("接口返回:[{}]", responseText);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responseText, PayUnifiedOrderResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("统一下单失败:" + e.getMessage());
            throw new RuntimeException("统一下单失败");
        }

    }


//    private String post(Map<String, Object> requestBody, String url) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        logger.info("接口请求:[{}]", requestBody);
//        HttpEntity<Map> entity = new HttpEntity<>(requestBody, headers);
//        return restTemplate.postForObject(url, entity, String.class);
//    }

    private String post(Map<String, Object> requestBody, String url) {

        String response = RestClient.create()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);
        return response;
    }


    /**
     * 预上传文件,上传文件之前要先调用此端口确定上传的目录和上传的url
     *
     * @param filePath
     * @return
     */
    public BeforeUploadFileResponse beforeUploadFile(String filePath) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("env", env);
            requestBody.put("path", filePath);
            logger.info("接口请求:[{}]", requestBody);
            String responseText = post(requestBody, beforeUploadFileUrl);
            logger.info("接口返回:[{}]", responseText);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responseText, BeforeUploadFileResponse.class);
        } catch (JsonProcessingException e) {
            logger.error("预上传文件失败:" + e.getMessage());
            throw new RuntimeException(e);

        }
    }

    public void uploadFile(BeforeUploadFileResponse beforeUploadFileResponse, String filePath, byte[] fileBytes) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("key", filePath);
            body.add("Signature", beforeUploadFileResponse.getAuthorization());
            body.add("x-cos-security-token", beforeUploadFileResponse.getToken());
            body.add("x-cos-meta-fileid", beforeUploadFileResponse.getCosFileId());
            // 将字节数组包装为 ByteArrayResource，并设置文件名
            ByteArrayResource fileResource = new ByteArrayResource(fileBytes) {
                @Override
                public String getFilename() {
                    return "test.png"; // 必须重写此方法，否则服务器可能无法获取文件名
                }
            };
            body.add("file", fileResource);

            String response = RestClient.create()
                    .post()
                    .uri(beforeUploadFileResponse.getUrl())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传文件失败:" + e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
