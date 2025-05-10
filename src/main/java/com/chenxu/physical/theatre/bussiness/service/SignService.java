package com.chenxu.physical.theatre.bussiness.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;

/**
 * @author mamingze
 * @version 1.0
 * @title SignService
 * @description
 * @create 2025/5/11 00:44
 */
@Service
public class SignService {
    private static final Logger logger = LoggerFactory.getLogger(SignService.class);

    //获取配置文件中的签名算法
    @Value("${sercurity.algorithm}")
    private String algorithm;

    public void sign(byte[] data, PrivateKey privateKey) throws NoSuchAlgorithmException {
        try {
            Signature signature = Signature.getInstance(algorithm);
            signature.initSign(privateKey);
            signature.update(data);
            signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("签名失败:" + e.getMessage());
        }
    }

    public void sign(String data, Signature signature) throws NoSuchAlgorithmException {
        try {

            signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("签名失败:" + e.getMessage());
        }
    }
}
