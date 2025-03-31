package org.kuraterut.verartitgbotformaster.service;

import lombok.Data;
import org.kuraterut.verartitgbotformaster.model.dto.AuthResponse;
import org.kuraterut.verartitgbotformaster.utils.UserSession;
import org.springframework.stereotype.Service;
import org.kuraterut.verartitgbotformaster.utils.BotState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionService {
    private final Map<Long, UserSession> userSessions = new ConcurrentHashMap<>();

    public UserSession getSession(Long chatId) {
        return userSessions.computeIfAbsent(chatId, k -> new UserSession());
    }

    public void updateSession(Long chatId, UserSession session) {
        userSessions.put(chatId, session);
    }
}