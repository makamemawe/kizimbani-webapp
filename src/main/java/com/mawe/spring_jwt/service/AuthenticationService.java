package com.mawe.spring_jwt.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mawe.spring_jwt.model.AuthenticationResponse;
import com.mawe.spring_jwt.model.Token;
import com.mawe.spring_jwt.model.User;
import com.mawe.spring_jwt.repository.TokenRepository;
import com.mawe.spring_jwt.repository.UserRepository;

@Service
public class AuthenticationService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    public AuthenticationService(UserRepository repository,
            PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager,
            TokenRepository tokenRepository) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse register(User request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        user = repository.save(user);

        String jwt = jwtService.generateToken(user);
        // save the generated token
        saveUserToken(user, jwt);

        return new AuthenticationResponse(jwt);
    }

    private void saveUserToken(User user, String jwt) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);

        revokeAllTokenByUser(user);

        saveUserToken(user, token);

        return new AuthenticationResponse(token);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokListByUser = tokenRepository.findAllTokenByUser(user.getId());

        if (!validTokListByUser.isEmpty()) {
            validTokListByUser.forEach(t -> {
                t.setLoggedOut(true);
            });

        }
        tokenRepository.saveAll(validTokListByUser);
    }

}
