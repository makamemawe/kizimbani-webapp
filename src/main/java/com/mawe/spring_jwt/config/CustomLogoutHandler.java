package com.mawe.spring_jwt.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.mawe.spring_jwt.model.Token;
import com.mawe.spring_jwt.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            return;
        }

        String token = authHeader.substring(7);

        // get stored token from the database
        Token storedToken = tokenRepository.findByToken(token).orElse(null);

        // invalidate the token i.e make logout true
        if (token != null) {
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);

        }
    }

}
