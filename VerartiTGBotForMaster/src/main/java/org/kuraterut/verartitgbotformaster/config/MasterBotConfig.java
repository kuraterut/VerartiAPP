package org.kuraterut.verartitgbotformaster.config;

import org.kuraterut.verartitgbotformaster.controller.MasterBotController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@DependsOn("masterBotController")
public class MasterBotConfig {

    private final MasterBotController bot;

    @Autowired
    public MasterBotConfig(MasterBotController bot) {
        this.bot = bot;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);  // Регистрация бота
        return botsApi;
    }
}
