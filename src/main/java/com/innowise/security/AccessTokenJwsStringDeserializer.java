package com.innowise.security;

import com.innowise.exceptions.TokenVerificationException;
import com.innowise.security.entities.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;


// signed encoded string -> verify sign -> decode string -> parse to Token
public class AccessTokenJwsStringDeserializer implements Function<String, Token> {
    private final JWSVerifier jwsVerifier;

    private final static Logger log = LoggerFactory.getLogger(AccessTokenJwsStringDeserializer.class);

    public AccessTokenJwsStringDeserializer(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    public Token apply(String string) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(string);
            if (signedJWT.verify(jwsVerifier)) {
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                return new Token(UUID.fromString(claimsSet.getJWTID()),
                        claimsSet.getIntegerClaim("userId"),
                        claimsSet.getSubject(),
                        claimsSet.getStringListClaim("authorities"),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant());
            }
        } catch (ParseException e ) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error while parsing token!", e);
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
            throw new TokenVerificationException("Token signature verification failed!");
        }

        return null;
    }

}