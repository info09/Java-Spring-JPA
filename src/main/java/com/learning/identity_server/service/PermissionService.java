package com.learning.identity_server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.learning.identity_server.dto.request.PermissionRequest;
import com.learning.identity_server.dto.response.PermissionResponse;
import com.learning.identity_server.mapper.IPermissionMapper;
import com.learning.identity_server.repository.IPermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    IPermissionRepository permissionRepository;
    IPermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        var permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }
}
