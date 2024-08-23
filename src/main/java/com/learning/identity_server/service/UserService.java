package com.learning.identity_server.service;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.dto.response.UserDto;
import com.learning.identity_server.enums.Role;
import com.learning.identity_server.exception.AppException;
import com.learning.identity_server.exception.ErrorCode;
import com.learning.identity_server.mapper.IUserMapper;
import com.learning.identity_server.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    IUserRepository _userRepository;
    IUserMapper _userMapper;
    PasswordEncoder passwordEncoder;

    public UserDto createRequest(@org.jetbrains.annotations.NotNull UserCreateRequest request) {
        if (_userRepository.existsByUserName(request.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);

        var user = _userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.User.name());
        user.setRoles(roles);

        return _userMapper.toUserDto(_userRepository.save(user));
    }

    public UserDto updateRequest(String userId, UserUpdateRequest request) {
        var user = _userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        _userMapper.updateUser(user, request);

        return _userMapper.toUserDto(_userRepository.save(user));
    }

    public List<UserDto> getAll() {
        return _userRepository.findAll().stream().map(_userMapper::toUserDto).toList();
    }

    public UserDto getByUserId(String id) {
        return _userMapper.toUserDto(_userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public void deleteUser(String id) {
        _userRepository.deleteById(id);
    }

    public UserDto getByUserName(String userName) {
        return _userMapper.toUserDto(_userRepository.findByuserName(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
