package com.learning.identity_server.mapper;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.dto.response.UserResponse;
import com.learning.identity_server.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    User toUser(UserCreateRequest request);

    UserResponse toUserDto(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
