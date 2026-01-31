package com.jdreyes.auth_service.bussines.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}