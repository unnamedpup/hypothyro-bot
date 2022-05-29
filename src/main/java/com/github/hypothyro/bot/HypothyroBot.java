package com.github.hypothyro.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HypothyroBot extends TelegramLongPollingBot {

    @Autowired
    Dispatcher dispatcher;

    private static final String botToken = "5490684858:AAGGlHtn9KE2jIkxKSRLpgm8x1psHeVNm2A";
    private static final String botUsername = "hypothyro_bot";

    @Override
    public void onUpdateReceived(Update update) {

        log.info("Update {}", update);
        SendMessage toSend = dispatcher.dispatch(update);

        try {
            execute(toSend);
        } catch(TelegramApiException e) {
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
