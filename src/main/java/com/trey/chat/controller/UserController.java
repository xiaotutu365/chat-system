package com.trey.chat.controller;

import com.trey.chat.client.FastDFSClient;
import com.trey.chat.model.User;
import com.trey.chat.model.bo.UserBo;
import com.trey.chat.service.UserService;
import com.trey.chat.utils.FileUtils;
import com.trey.chat.utils.JSONResult;
import lombok.extern.log4j.Log4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;



    @PostMapping("/uploadFaceBase64")
    public JSONResult uploadFaceBase64(@RequestBody UserBo userBo) {
        // 获取前端传过来的base64字符串，然后转换为文件对象再上传
        String base64Data = userBo.getFaceData();
        String userFacePath = "C:\\" + userBo.getUserId() + "_UserFace64.png";
        FileUtils.base64ToFile(userFacePath, base64Data);

        // 上传文件到fastdfs
        String url = null;
        try {
            MultipartFile faceFile = FileUtils.filetoMultipartFile(userFacePath);
            url = fastDFSClient.uploadBase64(faceFile);
            System.out.println("url: " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取缩略图的url
        String thump = "_80X80.";
        String[] strArr = url.split("\\.");
        String thumpImgUrl = strArr[0] + thump + strArr[1];

        // 更新用户头像
        User user = new User();
        user.setId(userBo.getUserId());
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(url);
        userService.updateUser(user);

        return JSONResult.ok(user);
    }
}