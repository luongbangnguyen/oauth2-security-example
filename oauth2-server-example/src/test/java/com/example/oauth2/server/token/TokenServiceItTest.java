package com.example.oauth2.server.token;

import com.example.oauth2.server.TestUtils;
import com.example.oauth2.server.exception.ErrorCode;
import com.example.oauth2.server.exception.ErrorCodeException;
import com.example.oauth2.server.jwt.JWTService;
import com.example.oauth2.server.rsa.RsaKeyService;
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
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@Profile("test")
public class TokenServiceItTest {

    private final TokenService tokenService;
    private final UserService userService;
    private final TestUtils testUtils;

    @MockBean
    private RsaKeyService rsaKeyService;

    @Autowired
    public TokenServiceItTest(TokenService tokenService, UserService userService, TestUtils testUtils) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.testUtils = testUtils;
    }

    @BeforeEach
    public void before() {
        this.testUtils.deleteAll();
    }

    @Test
    public void testCreateToken() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        mockRsaKeyService();
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        this.userService.createUser(username, password);
        Token token = this.tokenService.createToken(username, password);
        TokenVerification tokenVerification = this.tokenService.verifyAccessToken(token.accessToken);
        assertThat(tokenVerification.username).isEqualTo(username);
    }

    @Test
    public void testRefreshToken() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        mockRsaKeyService();
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        this.userService.createUser(username, password);
        Token token = this.tokenService.createToken(username, password);
        Token refreshToken = this.tokenService.refreshToken(token.refreshToken);
        assertThat(refreshToken.refreshToken).isEqualTo(token.refreshToken);
        TokenVerification verification = this.tokenService.verifyAccessToken(refreshToken.accessToken);
        assertThat(verification.username).isEqualTo(username);
    }

    private void mockRsaKeyService() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        when(rsaKeyService.getPrivateKey()).thenReturn((RSAPrivateKey) privateKey);
        when(rsaKeyService.getPublicKey()).thenReturn((RSAPublicKey) publicKey);
    }
}
