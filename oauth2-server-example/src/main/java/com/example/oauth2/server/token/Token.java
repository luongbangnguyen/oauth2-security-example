package com.example.oauth2.server.token;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("expires_in")
    public Long expiresIn;

    @JsonProperty("refresh_token")
    public String refreshToken;

    public static Token bearer(String accessToken, String refreshToken, Long expiresIn) {
        Token token = new Token();
        token.accessToken = accessToken;
        token.refreshToken = refreshToken;
        token.expiresIn = expiresIn;
        token.tokenType = "Bearer";
        return token;
    }
}
