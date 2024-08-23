package com.learning.identity_server.controller;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.dto.response.ApiResponse;
import com.learning.identity_server.dto.response.UserDto;
import com.learning.identity_server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService _userService;

    @PostMapping
    ApiResponse<UserDto> createUser(@RequestBody @Valid UserCreateRequest request) {
        var response = new ApiResponse<UserDto>();
        response.setResult(_userService.createRequest(request));
        return response;
    }

    @GetMapping
    ApiResponse<List<UserDto>> getAll() {
        var response = new ApiResponse<List<UserDto>>();
        response.setResult(_userService.getAll());
        return response;
    }

    @GetMapping("/getByUserName/{userName}")
    ApiResponse<UserDto> getByUserName(@PathVariable String userName) {
        var response = new ApiResponse<UserDto>();
        response.setResult(_userService.getByUserName(userName));
        return response;
    }

    @GetMapping("/{userId}")
    ApiResponse<UserDto> getById(@PathVariable("userId") String userId) {
        var response = new ApiResponse<UserDto>();
        response.setResult(_userService.getByUserId(userId));
        return response;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserDto> updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        var response = new ApiResponse<UserDto>();
        response.setResult(_userService.updateRequest(userId, request));
        return response;
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        var response = new ApiResponse<String>();
        _userService.deleteUser(userId);
        response.setMessage("Thành công");
        return response;
    }

    @GetMapping("/profile")
    ApiResponse<UserDto> getProfile() {
        var response = new ApiResponse<UserDto>();
        response.setResult(_userService.getProfile());
        return response;
    }
}
