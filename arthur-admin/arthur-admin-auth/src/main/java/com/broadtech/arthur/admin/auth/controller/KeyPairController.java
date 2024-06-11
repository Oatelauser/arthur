package com.broadtech.arthur.admin.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/16
 */
@RestController
public class KeyPairController {

    private KeyPair keyPair;

    @Autowired
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @GetMapping("/rsa/publicKey")
    public Object getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key);
    }
}
