package com.github.hypothyro.service.impl.stateCreators;

import com.github.hypothyro.bot.HypothyroBot;
import com.github.hypothyro.domain.Notification;
import com.github.hypothyro.service.NotificationStateCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.SneakyThrows;

@Service("NONE")
public class NoneState implements NotificationStateCreator {
    @Autowired private HypothyroBot bot;

    @SneakyThrows
    public void createState(Notification n) {
        SendMessage msg = SendMessage.builder()
            .chatId(n.getPatientId().toString())
            .text(n.getText())
            .build();

        bot.execute(msg);
    }
}


