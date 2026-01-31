package com.jdreyes.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jdreyes.auth_service.bussines.dtos.JwtResponse;
import com.jdreyes.auth_service.bussines.dtos.LoginRequest;
import com.jdreyes.auth_service.bussines.service.AuthService;
import com.jdreyes.auth_service.model.entity.ServiceSecret;
import com.jdreyes.auth_service.model.entity.User;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/token")
    public ResponseEntity<?> authenticateApp(@RequestHeader("X-Client-Id") String clientId, 
                                            @RequestHeader("X-Client-Secret") String clientSecret) {
        String jwt = authService.loginExternalApp(clientId, clientSecret);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register/user")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(user));
    }

    @PostMapping("/register/app")
    public ResponseEntity<ServiceSecret> registerApp(@RequestBody ServiceSecret app) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerExternalApp(app));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/app/{id}")
    public ResponseEntity<Void> removeApp(@PathVariable Long id) {
        authService.deleteExternalApp(id);
        return ResponseEntity.noContent().build();
    }
}
