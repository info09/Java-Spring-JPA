package com.learning.identity_server.dto.request;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Size(min = 5, message = "INVALID_USERNAME")
    String userName;
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
}
