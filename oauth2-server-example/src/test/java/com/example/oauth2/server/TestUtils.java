package com.example.oauth2.server;

import com.example.oauth2.server.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {
    private final UserRepository userRepository;

    public TestUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void deleteAll() {
        this.userRepository.deleteAll();
    }
}
