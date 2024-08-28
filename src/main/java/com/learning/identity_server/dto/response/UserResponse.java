package com.learning.identity_server.dto.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Set<RoleResponse> roles;
    String id;
    String userName;
    String firstName;
    String lastName;
    LocalDate dob;
}
