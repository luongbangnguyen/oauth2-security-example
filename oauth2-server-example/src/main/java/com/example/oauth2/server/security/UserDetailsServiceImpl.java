package com.example.oauth2.server.security;

import com.example.oauth2.server.user.User;
import com.example.oauth2.server.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optional = this.userRepository.findUserByUsername(username);
        User user = optional.orElseThrow(() -> new UsernameNotFoundException(username + " Notfound"));
        return new org.springframework.security.core.userdetails.User(user.username, user.password, Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
