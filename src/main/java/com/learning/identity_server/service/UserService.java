package com.learning.identity_server.service;

import com.learning.identity_server.constants.PredefineRole;
import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.dto.response.UserResponse;
import com.learning.identity_server.entity.Role;
import com.learning.identity_server.entity.User;
import com.learning.identity_server.exception.AppException;
import com.learning.identity_server.exception.ErrorCode;
import com.learning.identity_server.mapper.IUserMapper;
import com.learning.identity_server.repository.IRoleRepository;
import com.learning.identity_server.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
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
    IUserRepository userRepository;
    IRoleRepository roleRepository;
    IUserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createRequest(UserCreateRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefineRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserDto(user);
    }

    public UserResponse updateRequest(String userId, UserUpdateRequest request) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserDto(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN') || hasAuthority('CREATE_DATA')")
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).toList();
    }

    @PostAuthorize("returnObject.userName == authentication.name || hasRole('ADMIN')")
    public UserResponse getByUserId(String id) {
        return userMapper.toUserDto(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserResponse getByUserName(String userName) {
        return userMapper.toUserDto(userRepository
                .findByuserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse getProfile() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        var user = userRepository.findByuserName(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserDto(user);
    }
}
