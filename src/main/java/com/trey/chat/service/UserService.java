package com.trey.chat.service;

import com.trey.chat.model.User;
import com.trey.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean checkUsernameIsExist(String username) {
        User user = new User();
        user.setUsername(username);

        User result = userRepository.findByUsername(username);

        return result != null;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
