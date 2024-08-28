package com.learning.identity_server.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.dto.response.UserResponse;
import com.learning.identity_server.entity.Role;
import com.learning.identity_server.entity.User;
import com.learning.identity_server.exception.AppException;
import com.learning.identity_server.repository.IRoleRepository;
import com.learning.identity_server.repository.IUserRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private IUserRepository userRepository;

    @MockBean
    private IRoleRepository roleRepository;

    private UserUpdateRequest userUpdateRequest;
    private UserCreateRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;
    private List<Role> listRole;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1990, 1, 1);

        request = UserCreateRequest.builder()
                .userName("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("cf0600f538b3")
                .userName("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

        userUpdateRequest = UserUpdateRequest.builder()
                .firstName("Huy")
                .lastName("Tran")
                .dob(dob)
                .build();

        user = User.builder()
                .Id("cf0600f538b3")
                .userName("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

        listRole = new ArrayList<Role>();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.existsByUserName(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createRequest(request);
        // THEN

        Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
        Assertions.assertThat(response.getUserName()).isEqualTo("john");
    }

    @Test
    void createUser_userExisted_failed() {
        // GIVEN
        Mockito.when(userRepository.existsByUserName(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createRequest(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }

    @Test
    @WithMockUser(username = "admin")
    void getProfile_success() {
        // Given
        Mockito.when(userRepository.findByuserName(anyString())).thenReturn(Optional.of(user));

        // When
        var response = userService.getProfile();

        // Then
        Assertions.assertThat(response.getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    @WithMockUser(username = "admin")
    void getProfile_userNotFound_failed() {
        // Given
        Mockito.when(userRepository.findByuserName(anyString())).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(AppException.class, () -> userService.getProfile());

        // Then
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
    }

    @Test
    @WithMockUser(username = "admin")
    void getByUserName_success() {
        // Given
        Mockito.when(userRepository.findByuserName(anyString())).thenReturn(Optional.of(user));

        // When
        var response = userService.getByUserName(anyString());

        // Then
        Assertions.assertThat(response.getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    @WithMockUser(username = "admin")
    void getByUserName_userNotFound_failed() {
        // Given
        Mockito.when(userRepository.findByuserName(anyString())).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(AppException.class, () -> userService.getByUserName(anyString()));

        // Then
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAll_success() {
        // Given
        Mockito.when(userRepository.findByuserName(anyString())).thenReturn(Optional.empty());

        // When
        var responses = userService.getAll();

        // Then
        Assertions.assertThat((long) responses.size()).isNotNull();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getUserById_success() {
        // Given
        Mockito.when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        // When
        var responses = userService.getByUserId(anyString());

        // Then
        Assertions.assertThat(responses.getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getUserById_failed() {
        // Given
        Mockito.when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(AppException.class, () -> userService.getByUserId(anyString()));

        // Then
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteUser_failed() {
        // Given
        Mockito.when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // When
        userService.deleteUser(anyString());

        // Then
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateUser_success() {
        // Given
        Mockito.when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any())).thenReturn(user);
        Mockito.when(roleRepository.findAllById(ArgumentMatchers.any())).thenReturn(listRole);

        // When
        var response = userService.updateRequest(anyString(), userUpdateRequest);

        // Then
        Assertions.assertThat(response.getId()).isEqualTo(user.getId());
        Assertions.assertThat(response.getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateUser_failed() {
        // Given
        Mockito.when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // When
        var exception =
                assertThrows(AppException.class, () -> userService.updateRequest(anyString(), userUpdateRequest));

        // Then
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
    }
}
