package com.learning.identity_server.controller;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.dto.response.ApiResponse;
import com.learning.identity_server.dto.response.UserResponse;
import com.learning.identity_server.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService _userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        var response = new ApiResponse<UserResponse>();
        response.setResult(_userService.createRequest(request));
        return response;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("UserName: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        var response = new ApiResponse<List<UserResponse>>();
        response.setResult(_userService.getAll());
        return response;
    }

    @GetMapping("/getByUserName/{userName}")
    ApiResponse<UserResponse> getByUserName(@PathVariable String userName) {
        var response = new ApiResponse<UserResponse>();
        response.setResult(_userService.getByUserName(userName));
        return response;
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getById(@PathVariable("userId") String userId) {
        var response = new ApiResponse<UserResponse>();
        response.setResult(_userService.getByUserId(userId));
        return response;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        var response = new ApiResponse<UserResponse>();
        response.setResult(_userService.updateRequest(userId, request));
        return response;
    }

    @DeleteMapping("/{userId}")
    ApiResponse<Void> deleteUser(@PathVariable String userId) {
        _userService.deleteUser(userId);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/profile")
    ApiResponse<UserResponse> getProfile() {
        var response = new ApiResponse<UserResponse>();
        response.setResult(_userService.getProfile());
        return response;
    }
}
