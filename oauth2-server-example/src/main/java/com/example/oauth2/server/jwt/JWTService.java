package com.example.oauth2.server.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.oauth2.server.exception.ErrorCode;
import com.example.oauth2.server.exception.ErrorCodeException;
import com.example.oauth2.server.rsa.RsaKeyService;
import com.example.oauth2.server.token.TokenType;
import com.example.oauth2.server.user.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Date;

@Service
public class JWTService {

    private final String AUDIENCE = "oauth2Server";

    private final RsaKeyService rsaKeyService;

    public JWTService(RsaKeyService rsaKeyService) {
        this.rsaKeyService = rsaKeyService;
    }

    public String createToken(User user, long timeLifeSecond, TokenType tokenType) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Instant issuedAt = Instant.now();
        Instant expireAt = issuedAt.plusSeconds(timeLifeSecond);
        return JWT.create()
                .withIssuer(user.username)
                .withSubject(tokenType.name())
                .withAudience(AUDIENCE)
                .withIssuedAt(Date.from(issuedAt))
                .withExpiresAt(Date.from(expireAt))
                .withJWTId(user.id.toString())
                .sign(this.getAlgorithm());
    }

    private Algorithm getAlgorithm() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return Algorithm.RSA256(rsaKeyService.getPublicKey(), rsaKeyService.getPrivateKey());
    }

    private DecodedJWT decode(String token) {
        try {
            return JWT.require(this.getAlgorithm()).build().verify(token);
        } catch (TokenExpiredException e) {
            throw new ErrorCodeException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new ErrorCodeException(ErrorCode.INVALID_TOKEN);
        }
    }

    public DecodedJWT validateToken(String token, TokenType tokenType){
        DecodedJWT decodedJWT = this.decode(token);
        if (!tokenType.name().equals(decodedJWT.getSubject())) {
            throw new ErrorCodeException(ErrorCode.INVALID_TOKEN);
        }
        if (decodedJWT.getAudience().stream().noneMatch(AUDIENCE::equals)) {
            throw new ErrorCodeException(ErrorCode.AUDIENCE_NOT_MATCHING, AUDIENCE + " is require but found " + decodedJWT.getAudience().get(0));
        }
        return decodedJWT;
    }
}
