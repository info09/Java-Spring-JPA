package com.learning.identity_server.repository;

import com.learning.identity_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    boolean existsByUserName(String userName);

    Optional<User> findByuserName(String userName);
}
