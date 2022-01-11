package com.example.oauth2.server.initial;

import com.example.oauth2.server.rsa.RsaKeyConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;

@Component
@ConditionalOnProperty(value = "${rsa.key.initial.enable}", havingValue = "true")
public class RsaKeyInitial {

    private final RsaKeyConfig rsaKeyConfig;

    public RsaKeyInitial(RsaKeyConfig rsaKeyConfig) {
        this.rsaKeyConfig = rsaKeyConfig;
    }

    @EventListener
    public void initRsaKey(ContextRefreshedEvent event) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        File privateKeyFile = this.rsaKeyConfig.getPrivateKeyFile();
        File publicKeyFile = this.rsaKeyConfig.getPublicKeyFile();
        writeRsaKey(privateKey, privateKeyFile);
        writeRsaKey(publicKey, publicKeyFile);
    }

    private void writeRsaKey(Key key, File rsaFile) throws IOException {
        if (rsaFile.exists()) {
            return;
        }
        mkdir(rsaFile);
        Files.write(rsaFile.toPath(), key.getEncoded());
    }

    private void mkdir(File rsaFile) {
        File folder = rsaFile.getParentFile();
        if (folder.exists()) {
            return;
        }
        folder.mkdirs();
    }
}
