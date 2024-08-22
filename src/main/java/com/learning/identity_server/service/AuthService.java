package com.learning.identity_server.service;

import com.learning.identity_server.dto.request.AuthRequest;
import com.learning.identity_server.exception.AppException;
import com.learning.identity_server.exception.ErrorCode;
import com.learning.identity_server.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    IUserRepository _userRepository;

    public boolean Authenticated(AuthRequest request) {
        var user = _userRepository.findByuserName(request.getUserName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var passwordEncoder = new BCryptPasswordEncoder(10);
        return true;
    }
}
