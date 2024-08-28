package com.learning.identity_server.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.learning.identity_server.dto.request.RoleRequest;
import com.learning.identity_server.dto.response.RoleResponse;
import com.learning.identity_server.mapper.IRoleMapper;
import com.learning.identity_server.repository.IPermissionRepository;
import com.learning.identity_server.repository.IRoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    IRoleRepository _roleRepository;
    IPermissionRepository _permissionRepository;
    IRoleMapper _roleMapper;

    public RoleResponse Create(RoleRequest request) {
        var role = _roleMapper.toRole(request);
        var permissions = _permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = _roleRepository.save(role);
        return _roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        var roles = _roleRepository.findAll();
        return roles.stream().map(_roleMapper::toRoleResponse).toList();
    }

    public void delete(String roleName) {
        _roleRepository.deleteById(roleName);
    }
}
