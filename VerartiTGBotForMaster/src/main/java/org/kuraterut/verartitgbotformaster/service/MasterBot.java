package org.kuraterut.verartitgbotformaster.service;

import org.kuraterut.verartitgbotformaster.model.dto.AuthResponse;
import org.kuraterut.verartitgbotformaster.utils.BotState;
import org.kuraterut.verartitgbotformaster.utils.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MasterBot extends TelegramLongPollingBot {
    private final ApiService apiService;
    private final UserSessionService sessionService;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;

    @Autowired
    public MasterBot(ApiService apiService, UserSessionService sessionService) {
        this.apiService = apiService;
        this.sessionService = sessionService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        UserSession session = sessionService.getSession(chatId);

        try {
            switch (session.getCurrentState()) {
                case START:
                    handleStart(chatId, session);
                    break;
                case AWAITING_AUTHORIZATION_PHONE:
                    handlePhoneInput(chatId, messageText, session);
                    break;
                case AWAITING_AUTHORIZATION_PASSWORD:
                    handlePasswordInput(chatId, messageText, session);
                    break;
                case MAIN_MENU:
                    handleMainMenu(chatId, messageText, session);
                    break;
                case AWAITING_SCHEDULE_DATE:
                    handleScheduleDate(chatId, messageText, session);
                    break;
                case PROFILE_MENU:
                    handleProfileChoice(chatId, messageText, session);
                    break;
                case AWAITING_PROFILE_NEW_FIO:
                    handleNewName(chatId, messageText, session);
                    break;
                case AWAITING_PROFILE_NEW_BIO:
                    handleNewBio(chatId, messageText, session);
                    break;
            }
        } catch (Exception e) {
            sendMessage(chatId, "Произошла ошибка: " + e.getMessage());
            session.setCurrentState(BotState.START);
            sessionService.updateSession(chatId, session);
        }
    }

    private void handleStart(Long chatId, UserSession session) {
        session.setCurrentState(BotState.AWAITING_AUTHORIZATION_PHONE);
        sessionService.updateSession(chatId, session);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Добро пожаловать! Введите ваш номер телефона:");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handlePhoneInput(Long chatId, String phone, UserSession session) {
        session.setPhone(phone);
        session.setCurrentState(BotState.AWAITING_AUTHORIZATION_PASSWORD);
        sessionService.updateSession(chatId, session);

        sendMessage(chatId, "Теперь введите ваш пароль:");
    }

    private void handlePasswordInput(Long chatId, String password, UserSession session) {
        session.setPassword(password);

        try {
            AuthResponse authResponse = apiService.authenticate(session.getPhone(), session.getPassword());
            session.setAuthResponse(authResponse);
            session.setCurrentState(BotState.MAIN_MENU);
            sessionService.updateSession(chatId, session);

            showMainMenu(chatId);
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка авторизации: " + e.getMessage());
            session.setCurrentState(BotState.START);
            sessionService.updateSession(chatId, session);
            handleStart(chatId, session);
        }
    }

    private void showMainMenu(Long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Расписание");
        row1.add("Профиль");

        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Выберите действие:");
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleMainMenu(Long chatId, String messageText, UserSession session) {
        switch (messageText) {
            case "Расписание":
                session.setCurrentState(BotState.AWAITING_SCHEDULE_DATE);
                sessionService.updateSession(chatId, session);
                sendMessage(chatId, "Введите дату в формате dd.MM.yyyy (например, 15.05.2023):");
                break;
            case "Профиль":
                showProfileMenu(chatId, session);
                break;
            default:
                sendMessage(chatId, "Пожалуйста, используйте кнопки меню.");
        }
    }

    private void showProfileMenu(Long chatId, UserSession session) {
        session.setCurrentState(BotState.PROFILE_MENU);
        sessionService.updateSession(chatId, session);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Изменить ФИО");
        row1.add("Изменить биографию");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Назад");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Что вы хотите изменить?");
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleScheduleDate(Long chatId, String date, UserSession session) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate.parse(date, formatter); // Валидация даты

            String schedule = apiService.getSchedule(session.getAuthResponse().getToken(), date);
            sendMessage(chatId, "Расписание на " + date + ":\n" + schedule);

            session.setCurrentState(BotState.MAIN_MENU);
            sessionService.updateSession(chatId, session);
            showMainMenu(chatId);
        } catch (DateTimeParseException e) {
            sendMessage(chatId, "Неверный формат даты. Введите дату в формате dd.MM.yyyy (например, 15.05.2023):");
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка при получении расписания: " + e.getMessage());
            session.setCurrentState(BotState.MAIN_MENU);
            sessionService.updateSession(chatId, session);
            showMainMenu(chatId);
        }
    }

    private void handleProfileChoice(Long chatId, String choice, UserSession session) {
        switch (choice) {
            case "Изменить ФИО":
                session.setCurrentState(BotState.AWAITING_PROFILE_NEW_FIO);
                sessionService.updateSession(chatId, session);
                sendMessage(chatId, "Введите новое ФИО:");
                break;
            case "Изменить биографию":
                session.setCurrentState(BotState.AWAITING_PROFILE_NEW_BIO);
                sessionService.updateSession(chatId, session);
                sendMessage(chatId, "Введите новую биографию:");
                break;
            case "Назад":
                session.setCurrentState(BotState.MAIN_MENU);
                sessionService.updateSession(chatId, session);
                showMainMenu(chatId);
                break;
            default:
                sendMessage(chatId, "Пожалуйста, используйте кнопки меню.");
        }
    }

    private void handleNewName(Long chatId, String newName, UserSession session) {
        // Здесь реализация изменения ФИО через API
        sendMessage(chatId, "ФИО успешно изменено на: " + newName);
        session.setCurrentState(BotState.MAIN_MENU);
        sessionService.updateSession(chatId, session);
        showMainMenu(chatId);
    }

    private void handleNewBio(Long chatId, String newBio, UserSession session) {
        // Здесь реализация изменения биографии через API
        sendMessage(chatId, "Биография успешно изменена на: " + newBio);
        session.setCurrentState(BotState.MAIN_MENU);
        sessionService.updateSession(chatId, session);
        showMainMenu(chatId);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}