package com.jdreyes.auth_service.security;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AuditProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendAuditLog(String action, String username, String details) {
        Map<String, Object> message = new HashMap<>();
        message.put("action", action);
        message.put("username", username);
        message.put("details", details);
        message.put("timestamp", LocalDateTime.now().toString());
        message.put("service", "auth-service");

        rabbitTemplate.convertAndSend("audit_queue", message);
    }
}
