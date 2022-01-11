package com.example.oauth2.client.introspect;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifyTokenRequest {
    @JsonProperty("access_token")
    public String accessToken;
}
