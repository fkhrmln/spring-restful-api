package com.example.app.controller;

import com.example.app.dto.AccessTokenResponse;
import com.example.app.dto.LoginRequest;
import com.example.app.dto.Response;
import com.example.app.entity.User;
import com.example.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<AccessTokenResponse>> login(@RequestBody LoginRequest request) {
        AccessTokenResponse accessTokenResponse = authService.login(request);

        Response<AccessTokenResponse> response = Response.<AccessTokenResponse>builder()
                .status("OK")
                .message("Login Successfully")
                .data(accessTokenResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(
            path = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<?>> logout(User user) {
        authService.logout(user);

        Response<Object> response = Response.builder()
                .status("OK")
                .message("Logout Successfully")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
