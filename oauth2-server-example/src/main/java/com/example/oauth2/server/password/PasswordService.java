package com.example.oauth2.server.password;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encode(String password) {
        return this.passwordEncoder.encode(password);
    }

    public boolean verify(String password, String passwordEncrypted) {
        return this.passwordEncoder.matches(password, passwordEncrypted);
    }
}
