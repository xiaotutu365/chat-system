package com.trey.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.trey.chat.mapper")
public class ChatPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatPlatformApplication.class, args);
    }
}