package com.github.hypothyro.bot.impl.processors.command;

import com.github.hypothyro.bot.processors.CommandProcessor;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("start")
@Slf4j
public class StartCommandProcessor implements CommandProcessor {

    @Autowired
    private StateMachineCache stateCache;

    public SendMessage processCommand(Message msg) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(msg.getChatId().toString());
        toSend.setText("Привет, я бот для наблюдения за функцией щитовидной железы у оперированных пациентов или контроля заместительной терапии. Все рекомендации бота не являются врачебными решениями и должны рассматриваться, как повод к консультации специалиста. Давай знакомиться! Введите ваше имя");

        log.info("StartCommandProcessor");
        // TODO: Send req to psql to know about user info
        stateCache.setState(msg.getChatId(), PatientState.REGISTRATION_NAME);

        return toSend;
    }
}
