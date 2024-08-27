package com.learning.identity_server.service;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.response.UserResponse;
import com.learning.identity_server.entity.User;
import com.learning.identity_server.repository.IUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private IUserRepository userRepository;

    private UserCreateRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

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

        user = User.builder()
                .Id("cf0600f538b3")
                .userName("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
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

}
