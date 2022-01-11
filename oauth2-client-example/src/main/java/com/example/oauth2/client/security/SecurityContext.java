package com.example.oauth2.client.security;

import com.example.oauth2.client.exception.ErrorCode;
import com.example.oauth2.client.exception.ErrorCodeException;
import com.example.oauth2.client.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SecurityContext {
    public User getUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof DefaultOAuth2AuthenticatedPrincipal) {
            DefaultOAuth2AuthenticatedPrincipal auth2Authenticated = (DefaultOAuth2AuthenticatedPrincipal) principal;
            User user = new User();
            user.username = auth2Authenticated.getAttribute("username");
            user.authorities = auth2Authenticated.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            return user;
        }
        throw new ErrorCodeException(ErrorCode.USER_IS_ANONYMOUS);
    }
}
