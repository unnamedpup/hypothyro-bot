package com.github.hypothyro.bot.impl.handlers;

import java.util.Map;

import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.handlers.UpdateHandler;
import com.github.hypothyro.bot.processors.ControlCallbackProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ControlCallbackHandler implements UpdateHandler {

    @Autowired private Map<String, ControlCallbackProcessor> controlCallbackChain;
    @Autowired private StateMachineCache stateCache;

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String patientState = stateCache.getStateById(chatId).toString();

        log.info(controlCallbackChain.toString());
        log.info(patientState);

        SendMessage toSend = controlCallbackChain
            .get(patientState)
            .processRegistrationCallback(update.getCallbackQuery());

        return toSend;
    }
}

