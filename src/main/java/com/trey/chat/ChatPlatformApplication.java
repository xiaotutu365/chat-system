package com.trey.chat;

import com.trey.chat.utils.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChatPlatformApplication {

    @Bean
    public SpringUtils getSpringUtils() {
        return new SpringUtils();
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatPlatformApplication.class, args);
    }
}