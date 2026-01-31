package com.jdreyes.auth_service.bussines.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jdreyes.auth_service.model.entity.ServiceSecret;
import com.jdreyes.auth_service.model.entity.User;
import com.jdreyes.auth_service.model.repository.ServiceSecretRepository;
import com.jdreyes.auth_service.model.repository.UserRepository;
import com.jdreyes.auth_service.security.AuditProducer;
import com.jdreyes.auth_service.security.JwtUtils;

@Service
public class AuthService {
    @Autowired private AuditProducer auditProducer;
    @Autowired private UserRepository userRepository;
    @Autowired private ServiceSecretRepository externalAppRepository;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private PasswordEncoder passwordEncoder;

    public String loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (passwordEncoder.matches(password, user.getPassword())) {
            auditProducer.sendAuditLog("LOGIN_SUCCESS", username, "Login successful");
            return jwtUtils.generateTokenFromUsername(username);
        }
        auditProducer.sendAuditLog("LOGIN_FAILED", username, "Login failed");
        throw new RuntimeException("Wrong credentials");
    }

    public String loginExternalApp(String clientId, String clientSecret) {
        ServiceSecret app = externalAppRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("App does not registered"));
        
         
        if (passwordEncoder.matches(clientSecret, app.getClientSecret())) {
            auditProducer.sendAuditLog("LOGIN_SUCCESS", clientId, "Login successful");
            return jwtUtils.generateTokenForExternalApp(clientId, app.getScopes());
        }
        auditProducer.sendAuditLog("LOGIN_FAILED", clientId, "Login failed");
        throw new RuntimeException("Invalid client secret");
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        auditProducer.sendAuditLog("USER_REGISTERED", saved.getUsername(), "New user saved with ID: " + saved.getId());
        return saved;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        auditProducer.sendAuditLog("USER_DELETED", id.toString(), "User deleted with ID: " + id);
    }

    public ServiceSecret registerExternalApp(ServiceSecret app) {
        if (app.getClientId() == null) {
            app.setClientId(UUID.randomUUID().toString());
            auditProducer.sendAuditLog("APP_REGISTERED", app.getClientId(), "New app registered with ID: " + app.getClientId());
        }
        app.setClientSecret(passwordEncoder.encode(app.getClientSecret()));
        return externalAppRepository.save(app);
    }

    public void deleteExternalApp(Long id) {
        externalAppRepository.deleteById(id);
        auditProducer.sendAuditLog("APP_DELETED", id.toString(), "App deleted with ID: " + id);
    }
}