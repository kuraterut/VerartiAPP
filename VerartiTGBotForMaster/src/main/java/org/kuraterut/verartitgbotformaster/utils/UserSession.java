package org.kuraterut.verartitgbotformaster.utils;

import lombok.Data;
import org.kuraterut.verartitgbotformaster.model.dto.AuthResponse;

import java.time.LocalDateTime;

@Data
public class UserSession {
    private Long chatId;
    private String phone;
    private String password;
    private String token;
    private AuthState authState;
    private BotMenu currentMenu;

}