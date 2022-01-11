package com.example.oauth2.server.user;

import com.example.oauth2.server.exception.ErrorCode;
import com.example.oauth2.server.exception.ErrorCodeException;
import com.example.oauth2.server.password.PasswordService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordService passwordService;
    private final UserRepository userRepository;

    public UserService(PasswordService passwordService, UserRepository userRepository) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
    }

    public void createUser(String username, String password) {
        Optional<User> optional = this.userRepository.findUserByUsername(username);
        if (optional.isPresent()) {
            throw new ErrorCodeException(ErrorCode.USER_HAS_BEEN_REGISTERED);
        }
        String passwordEncode = this.passwordService.encode(password);
        User user = new User();
        user.username = username;
        user.password = passwordEncode;
        this.userRepository.save(user);
    }
}
