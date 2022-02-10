package com.group5.customerauthenticationservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtCreator {
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtCreator(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String createJwtForClaims(final String subject, final Map<String, String> claims) {
        final Calendar expiresAt = Calendar.getInstance();
        expiresAt.setTimeInMillis(Instant.now().toEpochMilli());
        expiresAt.add(Calendar.DATE, 1);

        final JWTCreator.Builder jwtBuilder = JWT.create().withSubject(subject);

        claims.forEach(jwtBuilder::withClaim);

        return jwtBuilder
                .withNotBefore(new Date())
                .withExpiresAt(expiresAt.getTime())
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }
}
