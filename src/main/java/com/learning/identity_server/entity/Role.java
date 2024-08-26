package com.learning.identity_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @jakarta.persistence.Id
    String name;
    String description;

    @ManyToMany
    Set<Permission> permissions;
}
