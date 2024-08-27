package com.learning.identity_server.controller;

import com.learning.identity_server.dto.request.AuthRequest;
import com.learning.identity_server.dto.request.IntrospectRequest;
import com.learning.identity_server.dto.request.LogoutRequest;
import com.learning.identity_server.dto.request.RefreshTokenRequest;
import com.learning.identity_server.dto.response.ApiResponse;
import com.learning.identity_server.dto.response.AuthResponse;
import com.learning.identity_server.dto.response.IntrospectResponse;
import com.learning.identity_server.service.AuthService;
import com.learning.identity_server.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/refresh")
    ApiResponse<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        var result = _authService.refreshToken(request);
        return ApiResponse.<AuthResponse>builder()
                .result(AuthResponse.builder()
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

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        _authService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}
