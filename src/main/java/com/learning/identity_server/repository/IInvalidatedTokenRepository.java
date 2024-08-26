package com.learning.identity_server.repository;

import com.learning.identity_server.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
