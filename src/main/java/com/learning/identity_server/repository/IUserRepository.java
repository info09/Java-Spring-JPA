package com.learning.identity_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.identity_server.entity.User;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    boolean existsByUserName(String userName);

    Optional<User> findByuserName(String userName);
}
