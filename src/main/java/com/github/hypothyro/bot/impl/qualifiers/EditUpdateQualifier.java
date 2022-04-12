package com.github.hypothyro.bot.impl.qualifiers;

import com.github.hypothyro.bot.handlers.UpdateHandler;
import com.github.hypothyro.bot.qualifiers.UpdateQualifier;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EditUpdateQualifier implements UpdateQualifier {

    @Autowired private UpdateHandler editUpdateHadler;
    @Autowired private StateMachineCache stateCache;

    public UpdateHandler qualify(Update update) {
        if (update.hasMessage() && controlStarted(update)) {
            log.info("edit started");
            return editUpdateHadler;
        }

        return null;
    }
    
    private boolean controlStarted(Update update) {
        PatientState patientState = stateCache.getStateById(
                update.getMessage().getChatId()
        );

        return patientState.toString().contains("EDIT_");
    }
}




