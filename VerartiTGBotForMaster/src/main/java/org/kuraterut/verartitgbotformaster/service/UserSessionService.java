package org.kuraterut.verartitgbotformaster.service;

import org.kuraterut.verartitgbotformaster.exception.UnauthorizedException;
import org.kuraterut.verartitgbotformaster.utils.AuthState;
import org.kuraterut.verartitgbotformaster.utils.UserSession;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class UserSessionService {
    private final Map<Long, UserSession> sessions = new ConcurrentHashMap<>();

    public UserSession getSession(Long chatId) {
        return sessions.get(chatId);
    }

    public void createSession(Long chatId, AuthState authState) {
        UserSession session = new UserSession();
        session.setChatId(chatId);
        session.setAuthState(authState);
        sessions.put(chatId, session);
    }

    public boolean isTokenExpired(UserSession session, ServerApiClient apiClient) {
        try{
            apiClient.validateToken(session.getToken());
            return false;
        } catch (UnauthorizedException e) {
            return true;
        }
    }
}