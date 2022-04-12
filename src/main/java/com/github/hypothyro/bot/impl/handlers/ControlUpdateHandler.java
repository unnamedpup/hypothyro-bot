package com.github.hypothyro.bot.impl.handlers;

import java.util.Map;

import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.handlers.UpdateHandler;
import com.github.hypothyro.bot.processors.ControlProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ControlUpdateHandler implements UpdateHandler {

    @Autowired private Map<String, ControlProcessor> controlProcessorChain;

    @Autowired private StateMachineCache stateCache;

    public SendMessage handle(Update update) {
        Message msg = update.getMessage();


        String patientState = stateCache.getStateById(msg.getChatId()).toString();

        log.info("Handling control text {}, state {}", msg.getText(), patientState);

        SendMessage toSend = controlProcessorChain.get(patientState).processRegistrationField(msg);

        return toSend;
    }

}


