package org.kuraterut.verartitgbotformaster.controller;

import jakarta.annotation.PostConstruct;
import org.kuraterut.verartitgbotformaster.exception.AuthException;
import org.kuraterut.verartitgbotformaster.model.dto.AuthResponse;
import org.kuraterut.verartitgbotformaster.model.dto.ProfileInfoRequest;
import org.kuraterut.verartitgbotformaster.model.dto.ScheduleResponse;
import org.kuraterut.verartitgbotformaster.model.entity.Appointment;
import org.kuraterut.verartitgbotformaster.model.entity.MasterInfo;
import org.kuraterut.verartitgbotformaster.service.ServerApiClient;
import org.kuraterut.verartitgbotformaster.service.UserSessionService;
import org.kuraterut.verartitgbotformaster.utils.AuthState;
import org.kuraterut.verartitgbotformaster.utils.BotMenu;
import org.kuraterut.verartitgbotformaster.utils.DateUtils;
import org.kuraterut.verartitgbotformaster.utils.UserSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MasterBotController extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @PostConstruct
    public void init() {
        if (botToken == null || botToken.isEmpty()) {
            throw new IllegalStateException("Bot token must not be null or empty");
        }
        if (botUsername == null || botUsername.isEmpty()) {
            throw new IllegalStateException("Bot username must not be null or empty");
        }
        System.out.println("Bot initialized with username: " + botUsername);
    }

    private final UserSessionService userSessionService;
    private final ServerApiClient serverApiClient;

    public MasterBotController(UserSessionService userSessionService, ServerApiClient serverApiClient) {
        this.userSessionService = userSessionService;
        this.serverApiClient = serverApiClient;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            try {
                handleMessage(chatId, messageText);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(Long chatId, String messageText) throws TelegramApiException {
        UserSession session = userSessionService.getSession(chatId);

        if (messageText.equals("/start") || (session != null && userSessionService.isTokenExpired(session))) {
            startAuthentication(chatId);
            return;
        }

        if (session == null || session.getAuthState() != AuthState.AUTHENTICATED) {
            handleAuthentication(chatId, messageText, session);
            return;
        }

        handleAuthenticatedUser(chatId, messageText, session);
    }

    private void startAuthentication(Long chatId) throws TelegramApiException {
        userSessionService.createSession(chatId, AuthState.WAITING_FOR_PHONE);
        sendMessage(chatId, "Добро пожаловать! Пожалуйста, введите ваш номер телефона:", null);
    }

    private void handleAuthentication(Long chatId, String messageText, UserSession session) throws TelegramApiException {
        if (session == null) {
            startAuthentication(chatId);
            return;
        }

        switch (session.getAuthState()) {
            case WAITING_FOR_PHONE:
                session.setPhone(messageText);
                session.setAuthState(AuthState.WAITING_FOR_PASSWORD);
                sendMessage(chatId, "Теперь введите ваш пароль:", null);
                break;
            case WAITING_FOR_PASSWORD:
                session.setPassword(messageText);
                try {
                    AuthResponse response = serverApiClient.authenticate(session.getPhone(), session.getPassword());
                    session.setToken(response.getToken());
                    session.setTokenExpiration(response.getExpiresAt());
                    session.setAuthState(AuthState.AUTHENTICATED);
                    session.setCurrentMenu(BotMenu.MAIN);
                    showMainMenu(chatId);
                } catch (AuthException e) {
                    sendMessage(chatId, "Ошибка авторизации: " + e.getMessage(), null);
                    startAuthentication(chatId);
                }
                break;
            default:
                startAuthentication(chatId);
        }
    }

    private void handleAuthenticatedUser(Long chatId, String messageText, UserSession session) throws TelegramApiException {
        if (session.getCurrentMenu() == null) {
            showMainMenu(chatId);
            return;
        }

        switch (session.getCurrentMenu()) {
            case MAIN:
                handleMainMenu(chatId, messageText, session);
                break;
            case SCHEDULE_DATE:
                handleScheduleDate(chatId, messageText, session);
                break;
            case PROFILE_EDIT:
                handleProfileEdit(chatId, messageText, session);
                break;
            case PROFILE_EDIT_FIO:
                handleProfileFioEdit(chatId, messageText, session);
                break;
            case PROFILE_EDIT_BIO:
                handleProfileBioEdit(chatId, messageText, session);
                break;
            case PROFILE_EDIT_PASSWORD:
                handleProfilePasswordEdit(chatId, messageText, session);
                break;
        }
    }

    private void handleMainMenu(Long chatId, String messageText, UserSession session) throws TelegramApiException {
        if (messageText.equals("Расписание")) {
            session.setCurrentMenu(BotMenu.SCHEDULE_DATE);
            sendMessage(chatId, "Введите дату в формате dd.MM.yyyy:", null);
        } else if (messageText.equals("Профиль")) {
            session.setCurrentMenu(BotMenu.PROFILE_EDIT);
            showProfileEditMenu(chatId);
        }
    }

    private void handleScheduleDate(Long chatId, String messageText, UserSession session) throws TelegramApiException {
        try {
            String formattedDate = DateUtils.convertToApiFormat(messageText);
            ScheduleResponse response = serverApiClient.getSchedule(session.getToken(), formattedDate);

            if (response.getAppointments().isEmpty()) {
                sendMessage(chatId, "В этот день работы нет", null);
            } else {
                StringBuilder sb = new StringBuilder("Расписание на " + messageText + ":\n\n");
                for (Appointment app : response.getAppointments()) {
                    sb.append(app.buildInfo());
                }
                sendMessage(chatId, sb.toString(), null);
            }
            session.setCurrentMenu(BotMenu.MAIN);
            showMainMenu(chatId);
        } catch (Exception e) {
            sendMessage(chatId, "Не понимаю. Я жду от вас дату в формате dd.MM.yyyy", null);
        }
    }

    private void handleProfileEdit(Long chatId, String messageText, UserSession session) throws TelegramApiException {
        if (messageText.equals("Изменить ФИО")) {
            session.setCurrentMenu(BotMenu.PROFILE_EDIT_FIO);
            sendMessage(chatId, "Введите новое ФИО(Три слова, разделенных пробелом. Если отчества нет, третьим словом поставьте -, составные части ФИО разделить -):", null);
        } else if (messageText.equals("Изменить биографию")) {
            session.setCurrentMenu(BotMenu.PROFILE_EDIT_BIO);
            sendMessage(chatId, "Введите новую биографию:", null);
        } else if (messageText.equals("Сменить пароль")) {
            session.setCurrentMenu(BotMenu.PROFILE_EDIT_PASSWORD);
            sendMessage(chatId, "Введите старый и новый пароль через пробел:", null);
        } else if (messageText.equals("Назад")) {
            session.setCurrentMenu(BotMenu.MAIN);
            showMainMenu(chatId);
        }
    }

    private void handleProfileFioEdit(Long chatId, String messageText, UserSession session) throws TelegramApiException {
        try {
            String[] fio = messageText.split(" ");
            if(fio.length != 3) throw new Exception("Неправильный формат ФИО");
            ProfileInfoRequest profileInfoRequest = new ProfileInfoRequest();
            profileInfoRequest.setName(fio[0]);
            profileInfoRequest.setSurname(fio[1]);
            if(!fio[2].equals("-")) profileInfoRequest.setPatronymic(fio[2]);

            serverApiClient.updateProfileInfo(session.getToken(), profileInfoRequest);
            sendMessage(chatId, "ФИО успешно обновлено", null);
            session.setCurrentMenu(BotMenu.PROFILE_EDIT);
            showProfileEditMenu(chatId);
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка при обновлении ФИО: " + e.getMessage(), null);
        }
    }

    private void handleProfileBioEdit(Long chatId, String messageText, UserSession session) throws TelegramApiException {
        try {
            serverApiClient.updateProfileBio(session.getToken(), messageText);
            sendMessage(chatId, "Биография успешно обновлена", null);
            session.setCurrentMenu(BotMenu.PROFILE_EDIT);
            showProfileEditMenu(chatId);
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка при обновлении биографии: " + e.getMessage(), null);
        }
    }

    private void handleProfilePasswordEdit(Long chatId, String messageText, UserSession session) throws TelegramApiException {
        try {
            serverApiClient.updateProfilePassword(session.getToken(), messageText);
            sendMessage(chatId, "Пароль успешно обновлен", null);
            session.setCurrentMenu(BotMenu.PROFILE_EDIT);
            showProfileEditMenu(chatId);
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка при обновлении пароля: " + e.getMessage(), null);
        }
    }

    private void showMainMenu(Long chatId) throws TelegramApiException {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Расписание");
        row1.add("Профиль");

        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        sendMessage(chatId, "Главное меню:", keyboardMarkup);
    }

    private void showProfileEditMenu(Long chatId) throws TelegramApiException {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Изменить ФИО");
        row1.add("Изменить биографию");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Сменить пароль");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("Назад");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        String messageText = "";
        try {
            MasterInfo master = serverApiClient.getProfileInfo(userSessionService.getSession(chatId).getToken());
            messageText = "Вот что я знаю о тебе: \n" + master.buildInfo();
        }
        catch (Exception ex){
            messageText = "Ошибка получения данных о профиле: " + ex.getMessage();
        }
        sendMessage(chatId, messageText, keyboardMarkup);
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        if (keyboard != null) {
            message.setReplyMarkup(keyboard);
        }
        execute(message);
    }
}