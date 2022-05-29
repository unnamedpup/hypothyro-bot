package com.github.hypothyro;

import com.github.hypothyro.bot.HypothyroBot;
import com.github.hypothyro.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApplicationStartup {

    @Autowired private HypothyroBot bot;
    @Autowired private NotificationService notifications;
    private boolean l = true;

    /**
     * This event is executed as late as conceivably possible to indicate that 
     * the application is ready to service requests.
     */
    @EventListener
    @SneakyThrows
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        try {
            log.info("Starting Hypothyro bot...");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            if (l == true) {
                botsApi.registerBot(bot);
            }

            notifications.updateNotifications();
            log.info("Bot started !");
        } catch (TelegramApiException e) {
            log.error("Failed to start bot: {}", e.getMessage());
        }
    }
 
}
