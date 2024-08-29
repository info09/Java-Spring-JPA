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
    IRoleRepository roleRepository;
    IPermissionRepository permissionRepository;
    IRoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
