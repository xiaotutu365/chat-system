package com.trey.chat.service;

import com.trey.chat.client.FastDFSClient;
import com.trey.chat.model.User;
import com.trey.chat.repository.UserRepository;
import com.trey.chat.utils.FileUtils;
import com.trey.chat.utils.QRCodeUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    public boolean checkUsernameIsExist(String username) {
        User user = new User();
        user.setUsername(username);

        User result = userRepository.findByUsername(username);

        return result != null;
    }

    /**
     * 保存用户信息
     *
     * @param user
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public User saveUser(User user) {
        String userId = Sid.nextShort();
        // 为每个用户生成一个唯一的二维码
        // 1、生成二维码的路径
        String qrCodePath = "C:\\user" + userId + "_qrcode.png";
        // 2、二维码生成格式：trey_qrcode:[username]
        qrCodeUtils.createQRCode(qrCodePath, "trey_qrcode:" + user.getUsername());
        MultipartFile qrCodeFile = FileUtils.filetoMultipartFile(qrCodePath);
        String qrCodeUrl = null;

        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        user.setQrcode(qrCodeUrl);
        user.setId(userId);
        userRepository.save(user);
        return user;
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}