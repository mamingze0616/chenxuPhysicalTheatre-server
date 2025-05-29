package com.chenxu.physical.theatre.configure;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author mamingze
 * @version 1.0
 * @title RestConfigure
 * @description
 * @create 2025/4/7 20:50
 */
//@Configuration
public class RestConfigure {
    //    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        // 添加 multipart 支持
        restTemplate.getMessageConverters().add(new AllEncompassingFormHttpMessageConverter());
        return restTemplate;
    }

    //    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(15000);
        // 设置代理
        return factory;
    }
}
