package com.jdreyes.auth_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jdreyes.auth_service.bussines.service.AuthService;
import com.jdreyes.auth_service.model.entity.User;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    @Lazy
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        System.out.println("DEBUG: Petición recibida en: " + request.getRequestURI());
        
        try {
            String jwt = parseJwt(request);
            if (jwt != null) {
                System.out.println("DEBUG: JWT detectado: " + jwt.substring(0, 10) + "...");
                if (jwtUtils.validateJwtToken(jwt)) {
                    String subject = jwtUtils.getSubjectFromJwtToken(jwt);
                    System.out.println("DEBUG: Token válido para usuario: " + subject);
                    
                    // Aquí se hace la magia
                    User userDetails = authService.getUserByUsername(subject).orElse(null);
                    if(userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("DEBUG: Autenticación establecida en SecurityContext");
                    }
                }
            } else {
                System.out.println("DEBUG: No se encontró header Authorization o Bearer");
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Error en filtro: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}