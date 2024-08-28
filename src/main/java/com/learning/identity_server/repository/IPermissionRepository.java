package com.learning.identity_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.identity_server.entity.Permission;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, String> {}
