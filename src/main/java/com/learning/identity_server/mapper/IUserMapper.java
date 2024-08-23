package com.learning.identity_server.mapper;

import com.learning.identity_server.dto.request.UserCreateRequest;
import com.learning.identity_server.dto.request.UserUpdateRequest;
import com.learning.identity_server.dto.response.UserDto;
import com.learning.identity_server.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    User toUser(UserCreateRequest request);

    UserDto toUserDto(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
