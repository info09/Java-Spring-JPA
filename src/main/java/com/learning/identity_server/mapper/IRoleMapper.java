package com.learning.identity_server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.learning.identity_server.dto.request.RoleRequest;
import com.learning.identity_server.dto.response.RoleResponse;
import com.learning.identity_server.entity.Role;

@Mapper(componentModel = "spring")
public interface IRoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
