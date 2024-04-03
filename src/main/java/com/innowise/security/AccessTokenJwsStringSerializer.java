package com.innowise.security;

import com.innowise.security.entities.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.function.Function;

//Token object -> build SignedJWT object -> try sign with JWSSigner -> serialize to String
public class AccessTokenJwsStringSerializer implements Function<Token, String> {
    private final JWSSigner jwsSigner;

    public void setJwsAlgorithm(JWSAlgorithm jwsAlgorithm) {
        this.jwsAlgorithm = jwsAlgorithm;
    }

    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

    public AccessTokenJwsStringSerializer(JWSSigner jwsSigner, JWSAlgorithm jwsAlgorithm) {
        this.jwsSigner = jwsSigner;
        this.jwsAlgorithm = jwsAlgorithm;
    }

    public AccessTokenJwsStringSerializer(JWSSigner jwsSigner) {
        this.jwsSigner = jwsSigner;
    }

    @Override
    public String apply(Token token) {
        var jwsHeader = new JWSHeader.Builder(this.jwsAlgorithm)
                .keyID(token.id().toString())
                .build();
        var claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .claim("userId", token.userId())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
        var signedJwt = new SignedJWT(jwsHeader, claimsSet);

        try {
            signedJwt.sign(this.jwsSigner);

            return signedJwt.serialize();
        }catch (JOSEException exception) {
            LoggerFactory.getLogger(this.getClass()).error(exception.getMessage(), exception);
        }

        return null;
    }
}
