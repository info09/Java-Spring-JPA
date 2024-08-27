package com.learning.identity_server.dto.request;

import com.learning.identity_server.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserCreateRequest {
    @Size(min = 5, message = "INVALID_USERNAME")
    String userName;
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;
}
