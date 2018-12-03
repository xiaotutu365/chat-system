package com.trey.chat.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class QRCodeUtils {

    @Value("qrcode.width")
    private int width;

    @Value("qrcode.height")
    private int height;

    @Value("qrcode.contentType")
    private String contentType;

    /**
     * 生成二维码
     * @param filePath
     * @param content
     */
    public void createQRCode(String filePath, String content) {
        // 定义二维码的参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 指定字符编码为UTF-8
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 指定二维码的纠错等级为中级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 设置图片的边距
        hints.put(EncodeHintType.MARGIN, 2);


        try {
            // 生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            Path path = new File(filePath).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, contentType, path);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
    }
}