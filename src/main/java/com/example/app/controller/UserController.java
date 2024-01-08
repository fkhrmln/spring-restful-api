package com.example.app.controller;

import com.example.app.dto.*;
import com.example.app.entity.User;
import com.example.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<?>> register(@RequestBody RegisterRequest request) {
        userService.register(request);

        Response<Object> response = Response.builder()
                .status("OK")
                .message("Register Successfully")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // @GetMapping(
    //         path = "/api/users/current",
    //         produces = MediaType.APPLICATION_JSON_VALUE
    // )
    // public ResponseEntity<Response<UserResponse>> currentUser(HttpServletRequest request) {
    //     User user = (User) request.getAttribute("user");
    //
    //     UserResponse userResponse = UserResponse.builder()
    //             .username(user.getUsername())
    //             .name(user.getName())
    //             .build();
    //
    //     Response<UserResponse> response = Response.<UserResponse>builder()
    //             .status("OK")
    //             .data(userResponse)
    //             .build();
    //
    //     return ResponseEntity.status(HttpStatus.OK).body(response);
    // }

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<UserResponse>> currentUser(User user) {
        UserResponse userResponse = userService.currentUser(user);

        Response<UserResponse> response = Response.<UserResponse>builder()
                .status("OK")
                .data(userResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(
            path = "/api/users/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<UserResponse>> updateUser(User user, @RequestBody UpdateUserRequest request) {
        UserResponse userResponse = userService.updateUser(user, request);

        Response<UserResponse> response = Response.<UserResponse>builder()
                .status("OK")
                .data(userResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
