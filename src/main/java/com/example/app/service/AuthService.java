package com.example.app.service;

import com.example.app.dto.AccessTokenResponse;
import com.example.app.dto.LoginRequest;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import com.example.app.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public AccessTokenResponse login(LoginRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username Not Registered"));

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Password");
        }

        String accessToken = UUID.randomUUID().toString();

        Long expiredAt = System.currentTimeMillis() + (1000 * 60 * 24 * 7);

        user.setAccessToken(accessToken);
        user.setExpiredAt(expiredAt);

        userRepository.save(user);

        return AccessTokenResponse.builder()
                .accessToken(user.getAccessToken())
                .expiredAt(user.getExpiredAt())
                .build();
    }

    @Transactional
    public void logout(User user) {
        user.setAccessToken(null);
        user.setExpiredAt(null);

        userRepository.save(user);
    }

}
