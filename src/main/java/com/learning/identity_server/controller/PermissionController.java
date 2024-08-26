package com.learning.identity_server.controller;

import com.learning.identity_server.dto.request.PermissionRequest;
import com.learning.identity_server.dto.response.ApiResponse;
import com.learning.identity_server.dto.response.PermissionResponse;
import com.learning.identity_server.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    private PermissionService _permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder().result(_permissionService.Create(request)).build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder().result(_permissionService.getAll()).build();
    }

    @DeleteMapping("/{permissionName}")
    ApiResponse<Void> delete(@PathVariable String permissionName) {
        _permissionService.delete(permissionName);
        return ApiResponse.<Void>builder().build();
    }
}
