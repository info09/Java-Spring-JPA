package com.learning.identity_server.controller;

import com.learning.identity_server.dto.request.AuthRequest;
import com.learning.identity_server.dto.request.IntrospectRequest;
import com.learning.identity_server.dto.response.ApiResponse;
import com.learning.identity_server.dto.response.AuthResponse;
import com.learning.identity_server.dto.response.IntrospectResponse;
import com.learning.identity_server.dto.response.UserDto;
import com.learning.identity_server.service.AuthService;
import com.learning.identity_server.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService _authService;
    UserService _userService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> Login(@RequestBody AuthRequest request) {
        var result = _authService.Authenticated(request);
        return ApiResponse.<AuthResponse>builder()
                .result(AuthResponse.builder()
                        .isAuthenticate(result.isAuthenticate())
                        .token(result.getToken())
                        .build())
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> Introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = _authService.Introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/profile")
    ApiResponse<UserDto> getProfile() {
        var response = new ApiResponse<UserDto>();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userName = authentication.getName();
        var userDto = _userService.getByUserName(userName);
        response.setResult(userDto);
        return response;
    }
}
