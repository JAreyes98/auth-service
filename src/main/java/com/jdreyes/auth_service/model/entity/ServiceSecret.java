package com.jdreyes.auth_service.model.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "external_apps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSecret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clientName; // Ej: "Laboratorio-Central"

    @Column(unique = true, nullable = false)
    private String clientId; // UUID o ID único

    @Column(nullable = false)
    private String clientSecret; // Hash de la clave

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> scopes; // read, write
}