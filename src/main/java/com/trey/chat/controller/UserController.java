package com.trey.chat.controller;

import com.trey.chat.model.User;
import com.trey.chat.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    public User saveUser(User user) {
        String userId = Sid.nextShort();
        // todo 为每个用户生成一个唯一的二维码
        user.setQrcode("");
        user.setId(userId);

        userService.saveUser(user);

        return user;
    }
}