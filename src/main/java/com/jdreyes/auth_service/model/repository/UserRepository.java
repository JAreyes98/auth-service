package com.jdreyes.auth_service.model.repository;

import java.util.Optional;
import com.jdreyes.auth_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
