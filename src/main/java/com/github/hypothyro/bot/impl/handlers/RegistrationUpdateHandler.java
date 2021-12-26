package com.github.hypothyro.bot.impl.handlers;

import java.util.Map;

import com.github.hypothyro.bot.handlers.UpdateHandler;
import com.github.hypothyro.bot.processors.RegistrationProcessor;
import com.github.hypothyro.bot.cache.states.StateMachineCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegistrationUpdateHandler implements UpdateHandler {

    @Autowired
    private Map<String, RegistrationProcessor> registrationProcessorChain;

    @Autowired
    private StateMachineCache stateCache;

    public SendMessage handle(Update update) {
        Message msg = update.getMessage();

        log.info("Handling registration text {}", msg.getText());

        String patientState = stateCache.getStateById(msg.getChatId()).toString();

        SendMessage toSend = registrationProcessorChain.get(patientState).processRegistrationField(msg);

        return toSend;
    }

}

