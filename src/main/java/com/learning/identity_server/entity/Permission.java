package com.learning.identity_server.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {
    @jakarta.persistence.Id
    String name;
    String description;
}
