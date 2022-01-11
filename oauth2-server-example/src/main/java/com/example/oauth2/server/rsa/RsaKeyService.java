package com.example.oauth2.server.rsa;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class RsaKeyService {

    private final RsaKeyConfig rsaKeyConfig;

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    public RsaKeyService(RsaKeyConfig rsaKeyConfig) {
        this.rsaKeyConfig = rsaKeyConfig;
    }

    public synchronized RSAPrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (privateKey != null) {
            return privateKey;
        }
        byte[] data = Files.readAllBytes(this.rsaKeyConfig.getPrivateKeyFile().toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(data);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        this.privateKey = (RSAPrivateKey) factory.generatePrivate(spec);
        return this.privateKey;
    }

    public synchronized RSAPublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (publicKey != null) {
            return publicKey;
        }
        byte[] data = Files.readAllBytes(this.rsaKeyConfig.getPublicKeyFile().toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        this.publicKey = (RSAPublicKey) factory.generatePublic(spec);
        return publicKey;
    }
}
