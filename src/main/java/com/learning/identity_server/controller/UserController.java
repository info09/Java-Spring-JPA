package com.learning.identity_server.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.dto.response.ApiResponse;
import com.learning.identity_server.dto.response.UserResponse;
import com.learning.identity_server.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        var response = new ApiResponse<UserResponse>();
        response.setResult(userService.createRequest(request));
        return response;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("UserName: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        var response = new ApiResponse<List<UserResponse>>();
        response.setResult(userService.getAll());
        return response;
    }

    @GetMapping("/getByUserName/{userName}")
    ApiResponse<UserResponse> getByUserName(@PathVariable String userName) {
        var response = new ApiResponse<UserResponse>();
        response.setResult(userService.getByUserName(userName));
        return response;
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getById(@PathVariable("userId") String userId) {
        var response = new ApiResponse<UserResponse>();
        response.setResult(userService.getByUserId(userId));
        return response;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(
            @PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        var response = new ApiResponse<UserResponse>();
        response.setResult(userService.updateRequest(userId, request));
        return response;
    }

    @DeleteMapping("/{userId}")
    ApiResponse<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/profile")
    ApiResponse<UserResponse> getProfile() {
        var response = new ApiResponse<UserResponse>();
        response.setResult(userService.getProfile());
        return response;
    }
}
