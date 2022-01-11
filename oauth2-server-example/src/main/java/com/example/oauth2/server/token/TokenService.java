package com.example.oauth2.server.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.oauth2.server.exception.ErrorCode;
import com.example.oauth2.server.exception.ErrorCodeException;
import com.example.oauth2.server.jwt.JWTService;
import com.example.oauth2.server.password.PasswordService;
import com.example.oauth2.server.user.User;
import com.example.oauth2.server.user.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;

@Service
public class TokenService {

    private final Long ACCESS_TOKEN_EXPIRE = 3600L;
    private final Long REFRESH_TOKEN_EXPIRE = 3600L * 24 * 365;

    private final PasswordService passwordService;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    public TokenService(PasswordService passwordService, UserRepository userRepository, JWTService jwtService) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public Token createToken(String clientId, String clientPassword) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        User user = this.userRepository.findUserByUsername(clientId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.CLIENT_ID_INVALID));

        boolean isMathPassword = this.passwordService.verify(clientPassword, user.password);
        if (!isMathPassword) {
            throw new ErrorCodeException(ErrorCode.CLIENT_ID_INVALID);
        }
        String accessToken = this.jwtService.createToken(user, ACCESS_TOKEN_EXPIRE, TokenType.ACCESS_TOKEN);
        String refreshToken = this.jwtService.createToken(user, REFRESH_TOKEN_EXPIRE, TokenType.REFRESH_TOKEN);
        return Token.bearer(accessToken, refreshToken, ACCESS_TOKEN_EXPIRE);
    }

    public Token refreshToken(String refreshToken) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        DecodedJWT decodedJWT = this.jwtService.validateToken(refreshToken, TokenType.REFRESH_TOKEN);
        User user = this.validateUser(decodedJWT);
        String accessToken = this.jwtService.createToken(user, ACCESS_TOKEN_EXPIRE, TokenType.ACCESS_TOKEN);
        return Token.bearer(accessToken, refreshToken, ACCESS_TOKEN_EXPIRE);
    }

    public TokenVerification verifyAccessToken(String accessToken) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        DecodedJWT decodedJWT = this.jwtService.validateToken(accessToken, TokenType.ACCESS_TOKEN);
        User user = this.validateUser(decodedJWT);
        TokenVerification verifying = new TokenVerification();
        verifying.userId = user.id;
        verifying.username = user.username;
        verifying.authorities = Collections.singletonList("USER");
        return verifying;
    }

    private User validateUser(DecodedJWT decodedJWT) {
        String username = decodedJWT.getIssuer();
        Long userId = Long.valueOf(decodedJWT.getId());
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.INVALID_TOKEN));
        if (StringUtils.equals(username, user.username)) {
            return user;
        }
        throw new ErrorCodeException(ErrorCode.INVALID_TOKEN);
    }
}
