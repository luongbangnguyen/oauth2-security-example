package com.example.oauth2.server.token;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenVerificationRequest {
    @JsonProperty("access_token")
    public String accessToken;
}
