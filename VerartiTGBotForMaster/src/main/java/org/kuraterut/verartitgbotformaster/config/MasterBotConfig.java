package org.kuraterut.verartitgbotformaster.config;

import org.kuraterut.verartitgbotformaster.service.MasterBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class MasterBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(MasterBot bot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);  // Регистрация бота
        return botsApi;
    }
}
