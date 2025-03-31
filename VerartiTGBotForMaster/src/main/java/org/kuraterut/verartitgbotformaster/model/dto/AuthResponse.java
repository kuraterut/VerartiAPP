package org.kuraterut.verartitgbotformaster.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthResponse {
    private String token;
    private LocalDateTime expiryDateTime;
}
