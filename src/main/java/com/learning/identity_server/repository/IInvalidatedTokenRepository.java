package com.learning.identity_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.identity_server.entity.InvalidatedToken;

public interface IInvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
