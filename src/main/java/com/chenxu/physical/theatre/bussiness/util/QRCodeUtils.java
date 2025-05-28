package com.chenxu.physical.theatre.bussiness.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mamingze
 * @version 1.0
 * @title QRCodeUtils
 * @description
 * @create 2025/5/28 15:38
 */
public class QRCodeUtils {
    private static final Logger logger = LoggerFactory.getLogger(QRCodeUtils.class);

    public static byte[] generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {
        logger.info("开始生成二维码 ,text:{}", text);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        logger.info("结束生成二维码 ,text:{}", text);
        return pngOutputStream.toByteArray();
    }

    public static byte[] generateQRCodeImage(String text)
            throws WriterException, IOException {
        return generateQRCodeImage(text, 250, 250);
    }
}
