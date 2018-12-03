package com.trey.chat.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class FileUtils {

    private static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5 * 1000);
            InputStream inputStream = connection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * base64转化为文件
     * @param filePath 文件路径
     * @param base64Data base64数据
     * @return
     */
    public static boolean base64ToFile(String filePath, String base64Data) {
        String data;

        if (base64Data == null || "".equals(base64Data)) {
            return false;
        } else {
            String[] dataSplit = base64Data.split("base64,");
            if (dataSplit.length == 2) {
                data = dataSplit[1];
            } else {
                return false;
            }
        }

        byte[] decodes = Base64Utils.decodeFromString(data);
        try {
            org.apache.commons.io.FileUtils.writeByteArrayToFile(new File(filePath), decodes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static MultipartFile filetoMultipartFile(String filePath) {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(new File(filePath));
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), "png", "images", fis);
            return multipartFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // MockMultipartFile mockMultipartFile = new MockMultipartFile();

    }
}