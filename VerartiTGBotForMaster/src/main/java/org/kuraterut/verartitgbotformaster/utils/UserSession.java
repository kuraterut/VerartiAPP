package org.kuraterut.verartitgbotformaster.utils;

import lombok.Data;
import org.kuraterut.verartitgbotformaster.model.dto.AuthResponse;

@Data
public class UserSession {
    private String phone;
    private String password;
    private AuthResponse authResponse;
    private BotState currentState = BotState.START;
}