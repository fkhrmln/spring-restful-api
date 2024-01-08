package com.example.app.service;

import com.example.app.dto.RegisterRequest;
import com.example.app.dto.UpdateUserRequest;
import com.example.app.dto.UserResponse;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import com.example.app.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username Already Exists");
        }

        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        User user = User.builder()
                .username(request.getUsername())
                .password(hashedPassword)
                .name(request.getName())
                .build();

        userRepository.save(user);
    }

    public User getUserByAccessToken(String accessToken) {
        User user = userRepository.findFirstByAccessTokenEquals(accessToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        return user;
    }

    public UserResponse currentUser(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse updateUser(User user, UpdateUserRequest request) {
        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getPassword() != null) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        userRepository.save(user);

        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

}
