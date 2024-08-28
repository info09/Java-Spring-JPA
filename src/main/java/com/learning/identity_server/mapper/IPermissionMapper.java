package com.learning.identity_server.mapper;

import org.mapstruct.Mapper;

import com.learning.identity_server.dto.request.PermissionRequest;
import com.learning.identity_server.dto.response.PermissionResponse;
import com.learning.identity_server.entity.Permission;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
