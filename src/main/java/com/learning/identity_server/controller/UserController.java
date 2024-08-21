package com.learning.identity_server.controller;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.entity.User;
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
    User createUser(@RequestBody @Valid UserCreateRequest request){
        return _userService.createRequest(request);
    }

    @GetMapping
    List<User> getAll(){
        return _userService.getAll();
    }

    @GetMapping("/{userId}")
    User getById(@PathVariable("userId") String userId){
        return _userService.getByUserId(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request){
        return _userService.updateRequest(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        return _userService.deleteUser(userId);
    }
}
