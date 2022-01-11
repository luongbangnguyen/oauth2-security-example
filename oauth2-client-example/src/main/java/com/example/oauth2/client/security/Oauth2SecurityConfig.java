package com.example.oauth2.client.security;

import com.example.oauth2.client.introspect.OpaqueTokenIntrospectorImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class Oauth2SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OpaqueTokenIntrospectorImpl opaqueTokenIntrospector;

    public Oauth2SecurityConfig(OpaqueTokenIntrospectorImpl opaqueTokenIntrospector) {
        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                .permitAll().anyRequest().authenticated()
                .and().oauth2ResourceServer()
                .opaqueToken().introspector(opaqueTokenIntrospector);
    }
}
