package com.example.oauth2.server.token;

import com.example.oauth2.server.exception.ErrorCode;
import com.example.oauth2.server.exception.ErrorCodeException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public Token createToken(@RequestParam ("grant_type") GrantType grantType,
                             @RequestParam (value = "client_id", required = false) String clientId,
                             @RequestParam (value = "client_secret", required = false) String clientSecret,
                             @RequestParam(value = "refresh_token", required = false) String refreshToken) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        switch (grantType) {
            case password: return this.tokenService.createToken(clientId, clientSecret);
            case refresh: return this.tokenService.refreshToken(refreshToken);
            default: throw new ErrorCodeException(ErrorCode.GRANT_TYPE_NOT_SUPPORT);
        }
    }

    @PostMapping("/verifier")
    public TokenVerification verifyToken(@RequestBody TokenVerificationRequest request) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return this.tokenService.verifyAccessToken(request.accessToken);
    }
}
