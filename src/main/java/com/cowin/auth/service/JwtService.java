package com.cowin.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    @Value("${security.jwt.private-key}")
    private String privateKeyPem;

    @Value("${security.jwt.public-key}")
    private String publicKeyPem;

    @Value("${security.jwt.expiration-seconds:3600}")
    private long expirySeconds;

    public String generateToken(String mobileNumber) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(mobileNumber)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirySeconds)))
                .signWith(getPrivateKey())
                .compact();
    }

    public Claims validateAndParse(String token) {
        return Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(token).getPayload();
    }

    public long getExpirySeconds() { return expirySeconds; }

    private PrivateKey getPrivateKey() {
        try {
            String key = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(key);
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        } catch (Exception e) {
            throw new IllegalStateException("Invalid private key", e);
        }
    }

    private PublicKey getPublicKey() {
        try {
            String key = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(key);
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        } catch (Exception e) {
            throw new IllegalStateException("Invalid public key", e);
        }
    }
}
