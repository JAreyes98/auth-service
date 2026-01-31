package com.jdreyes.auth_service.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jdreyes.auth_service.model.entity.ServiceSecret;

public interface ServiceSecretRepository extends JpaRepository<ServiceSecret, Long> {
    Optional<ServiceSecret> findByClientId(String clientId);
}