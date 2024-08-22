package com.learning.identity_server.controller;

import com.learning.identity_server.dto.request.AuthRequest;
import com.learning.identity_server.dto.response.ApiResponse;
import com.learning.identity_server.dto.response.AuthResponse;
import com.learning.identity_server.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService _authService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> Login(@RequestBody AuthRequest request) {
        var result = _authService.Authenticated(request);
        return ApiResponse.<AuthResponse>builder()
                .result(AuthResponse.builder()
                        .isAuthenticate(result)
                        .build())
                .build();
    }
}
