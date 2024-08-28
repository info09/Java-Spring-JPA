package com.learning.identity_server.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.learning.identity_server.constants.PredefineRole;
import com.learning.identity_server.entity.Role;
import com.learning.identity_server.entity.User;
import com.learning.identity_server.repository.IRoleRepository;
import com.learning.identity_server.repository.IUserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(IUserRepository _userRepository, IRoleRepository _roleRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (_userRepository.findByuserName(ADMIN_USER_NAME).isEmpty()) {
                var roleAdmin = _roleRepository.save(Role.builder()
                        .name(PredefineRole.ADMIN_ROLE)
                        .description("Admin Role")
                        .build());

                _roleRepository.save(Role.builder()
                        .name(PredefineRole.USER_ROLE)
                        .description("User Role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(roleAdmin);

                var user = User.builder()
                        .userName("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                _userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
}
