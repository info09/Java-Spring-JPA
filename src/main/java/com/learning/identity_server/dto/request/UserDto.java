package com.learning.identity_server.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class UserDto {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private LocalDate dob;
}
