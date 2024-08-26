package com.learning.identity_server.service;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.dto.response.UserResponse;
import com.learning.identity_server.exception.AppException;
import com.learning.identity_server.exception.ErrorCode;
import com.learning.identity_server.mapper.IUserMapper;
import com.learning.identity_server.repository.IRoleRepository;
import com.learning.identity_server.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    IUserRepository _userRepository;
    IRoleRepository _roleRepository;
    IUserMapper _userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createRequest(@NotNull UserCreateRequest request) {
        if (_userRepository.existsByUserName(request.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);

        var user = _userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return _userMapper.toUserDto(_userRepository.save(user));
    }

    public UserResponse updateRequest(String userId, UserUpdateRequest request) {
        var user = _userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        _userMapper.updateUser(user, request);

        var roles = _roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return _userMapper.toUserDto(_userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN') || hasAuthority('CREATE_DATA')")
    public List<UserResponse> getAll() {
        return _userRepository.findAll().stream().map(_userMapper::toUserDto).toList();
    }

    @PostAuthorize("returnObject.userName == authentication.name || hasRole('ADMIN')")
    public UserResponse getByUserId(String id) {
        return _userMapper.toUserDto(_userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public void deleteUser(String id) {
        _userRepository.deleteById(id);
    }

    public UserResponse getByUserName(String userName) {
        return _userMapper.toUserDto(_userRepository.findByuserName(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse getProfile() {
        var context = SecurityContextHolder.getContext();
        var userName = context.getAuthentication().getName();
        return _userMapper.toUserDto(_userRepository.findByuserName(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
