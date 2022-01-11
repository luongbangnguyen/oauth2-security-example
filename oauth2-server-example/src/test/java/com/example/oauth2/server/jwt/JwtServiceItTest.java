package com.example.oauth2.server.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.oauth2.server.TestUtils;
import com.example.oauth2.server.exception.ErrorCode;
import com.example.oauth2.server.exception.ErrorCodeException;
import com.example.oauth2.server.rsa.RsaKeyService;
import com.example.oauth2.server.token.TokenType;
import com.example.oauth2.server.user.User;
import com.example.oauth2.server.user.UserRepository;
import com.example.oauth2.server.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@Profile("test")
public class JwtServiceItTest {

    private final UserService userService;
    private final TestUtils testUtils;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    @MockBean
    private RsaKeyService rsaKeyService;

    @Autowired
    public JwtServiceItTest(UserService userService, TestUtils testUtils, UserRepository userRepository, JWTService jwtService) {
        this.userService = userService;
        this.testUtils = testUtils;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @BeforeEach
    public void before() {
        this.testUtils.deleteAll();
    }

    @Test
    public void testVerifyTokenExpire() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InterruptedException {
        mockRsaKeyService();
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        this.userService.createUser(username, password);
        User user = this.userRepository.findUserByUsername(username).get();
        String token = jwtService.createToken(user, 1, TokenType.ACCESS_TOKEN);
        Thread.sleep(2000);
        ErrorCodeException exception = assertThrows(ErrorCodeException.class, () -> jwtService.validateToken(token, TokenType.ACCESS_TOKEN));
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.TOKEN_EXPIRED.name());
    }

    @Test
    public void testVerifyTokenInvalid() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this.mockRsaKeyService();
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        this.userService.createUser(username, password);
        User user = this.userRepository.findUserByUsername(username).get();
        String token = jwtService.createToken(user, 3600, TokenType.ACCESS_TOKEN);
        ErrorCodeException exception = assertThrows(ErrorCodeException.class, () -> jwtService.validateToken(token, TokenType.REFRESH_TOKEN));
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_TOKEN.name());
    }

    @Test
    public void testVerifyAnotherToken() throws NoSuchAlgorithmException {
        String token = JWT.create()
                .withIssuer(UUID.randomUUID().toString())
                .withSubject(UUID.randomUUID().toString())
                .withAudience(UUID.randomUUID().toString())
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(3600)))
                .withJWTId(UUID.randomUUID().toString())
                .sign(this.getAlgorithm());
        ErrorCodeException exception = assertThrows(ErrorCodeException.class, () -> this.jwtService.validateToken(token, TokenType.ACCESS_TOKEN));
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_TOKEN.name());
    }

    private Algorithm getAlgorithm() throws NoSuchAlgorithmException {
        KeyPair keyPair = this.getKeyPair();
        return Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
    }

    private void mockRsaKeyService() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        KeyPair keyPair = getKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        when(rsaKeyService.getPrivateKey()).thenReturn((RSAPrivateKey) privateKey);
        when(rsaKeyService.getPublicKey()).thenReturn((RSAPublicKey) publicKey);
    }

    private KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }
}
