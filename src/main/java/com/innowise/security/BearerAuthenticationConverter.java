package com.innowise.security;

import com.innowise.security.entities.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.function.Function;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BearerAuthenticationConverter implements AuthenticationConverter {

    private Function<String, Token> accessTokenDeserializer;

    @Override
    public Authentication convert(HttpServletRequest request) {
        if(request.getHeader(HttpHeaders.AUTHORIZATION) != null && request.getHeader(HttpHeaders.AUTHORIZATION).startsWith("Bearer ")) {
            String accessTokenString = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);

            Token accessToken = accessTokenDeserializer.apply(accessTokenString);

            if(accessToken != null) {
                return new PreAuthenticatedAuthenticationToken(accessToken, accessTokenString);
            }
        }

        return null;
    }
}
