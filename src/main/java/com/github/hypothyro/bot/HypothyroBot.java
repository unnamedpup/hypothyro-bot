package com.github.hypothyro.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class HypothyroBot extends TelegramLongPollingBot {

    @Autowired
    Dispatcher dispatcher;

    private static final String botToken = "2006315544:AAFN23XTt2IFDkBmhKdjZPp7PB6jzswm2Bs";
    private static final String botUsername = "hypothyro_bot";

    @Override
    public void onUpdateReceived(Update update) {
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
