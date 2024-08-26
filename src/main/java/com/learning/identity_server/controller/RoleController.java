package com.learning.identity_server.controller;

import com.learning.identity_server.dto.request.RoleRequest;
import com.learning.identity_server.dto.response.ApiResponse;
import com.learning.identity_server.dto.response.RoleResponse;
import com.learning.identity_server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService _roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder().result(_roleService.Create(request)).build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder().result(_roleService.getAll()).build();
    }

    @DeleteMapping("/{permissionName}")
    ApiResponse<Void> delete(@PathVariable String permissionName) {
        _roleService.delete(permissionName);
        return ApiResponse.<Void>builder().build();
    }
}
