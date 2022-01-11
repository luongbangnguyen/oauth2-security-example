package com.example.oauth2.client.introspect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OpaqueTokenIntrospectorImpl implements OpaqueTokenIntrospector {

    @Value("${oauth2.token.verify.url}")
    private String tokenVerifyUrl;

    private final RestTemplate restTemplate;

    public OpaqueTokenIntrospectorImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        VerifyTokenRequest request = new VerifyTokenRequest();
        try {
            request.accessToken = token;
            ResponseEntity<TokenVerification> response = this.restTemplate.postForEntity(this.tokenVerifyUrl, request, TokenVerification.class);
            TokenVerification tokenVerification = response.getBody();
            Map<String, Object> attributes = new HashMap<>();
            assert tokenVerification != null;
            attributes.put("userId", tokenVerification.userId);
            attributes.put("username", tokenVerification.username);
            Collection<GrantedAuthority> roles = tokenVerification.authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            return new DefaultOAuth2AuthenticatedPrincipal(attributes, roles);
        } catch (Exception exception) {
            throw new OAuth2AuthenticationException(new OAuth2Error("Token in valid"));
        }
    }
}
