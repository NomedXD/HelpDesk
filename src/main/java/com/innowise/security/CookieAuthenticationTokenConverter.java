package com.innowise.security;

import com.innowise.security.entities.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.function.Function;
import java.util.stream.Stream;

@Setter
public class CookieAuthenticationTokenConverter implements AuthenticationConverter {

    private Function<String, Token> tokenCookieStringDeserializer;

    public CookieAuthenticationTokenConverter(Function<String, Token> tokenCookieStringDeserializer) {
        this.tokenCookieStringDeserializer = tokenCookieStringDeserializer;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        if(request.getCookies() != null) {
            return Stream.of(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("__Host-auth-token"))
                    .findFirst()
                    .map(cookie -> {
                        return new PreAuthenticatedAuthenticationToken(
                                this.tokenCookieStringDeserializer.apply(cookie.getValue()),
                                cookie.getValue()
                        );
                    }).orElse(null);
        }

        return null;
    }
}
