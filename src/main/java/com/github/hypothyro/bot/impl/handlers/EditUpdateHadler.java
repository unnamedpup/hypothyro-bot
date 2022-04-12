package com.github.hypothyro.bot.impl.handlers;

import java.util.Map;

import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.handlers.UpdateHandler;
import com.github.hypothyro.bot.processors.EditMessageProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EditUpdateHadler implements UpdateHandler {

    @Autowired private Map<String, EditMessageProcessor> editProcessorChain;
    @Autowired private StateMachineCache stateCache;


    public SendMessage handle(Update update) {
        Message msg = update.getMessage();

        String patientState = stateCache.getStateById(msg.getChatId()).toString();

        log.info("Handling edit text {}, state {}", msg.getText(), patientState);

        SendMessage toSend = editProcessorChain.get(patientState).processEditMessage(msg);

        return toSend;
    }

}


