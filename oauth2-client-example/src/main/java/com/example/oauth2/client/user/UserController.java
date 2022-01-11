package com.example.oauth2.client.user;

import com.example.oauth2.client.security.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final SecurityContext securityContext;

    public UserController(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @GetMapping("/info")
    public User getUserInfo() {
        return this.securityContext.getUserLogin();
    }
}
