package com.example.oauth2.server.rsa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class RsaKeyConfig {
    @Value("${rsa.key.storage.folder}")
    private String rsaFolder;

    public File getPrivateKeyFile() {
        String PRIVATE_KEY_FILE_NAME = "id_rsa.key";
        return new File(rsaFolder + "/" + PRIVATE_KEY_FILE_NAME);
    }

    public File getPublicKeyFile() {
        String PUBLIC_KEY_FILE_NAME = "id_rsa.pub";
        return new File(rsaFolder + "/" + PUBLIC_KEY_FILE_NAME);
    }
}
