package com.learning.identity_server.mapper;

import com.learning.identity_server.dto.request.PermissionRequest;
import com.learning.identity_server.dto.response.PermissionResponse;
import com.learning.identity_server.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
