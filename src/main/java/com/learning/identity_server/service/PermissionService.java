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
    IPermissionRepository _permissionRepository;
    IPermissionMapper _permissionMapper;

    public PermissionResponse Create(PermissionRequest request) {
        var permission = _permissionMapper.toPermission(request);
        permission = _permissionRepository.save(permission);
        return _permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = _permissionRepository.findAll();
        return permissions.stream().map(_permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permissionName) {
        _permissionRepository.deleteById(permissionName);
    }
}
