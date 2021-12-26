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
public class RegistrationUpdateQualifier implements UpdateQualifier {

    @Autowired
    private UpdateHandler registrationUpdateHandler;

    @Autowired
    private StateMachineCache stateCache;

    public UpdateHandler qualify(Update update) {
        if (update.hasMessage() && registrationStarted(update)) {
            log.info("Registration strted");
            return registrationUpdateHandler;
        }

        return null;
    }

    
    private boolean registrationStarted(Update update) {
        PatientState patientState = stateCache.getStateById(
                update.getMessage().getChatId()
        );

        return patientState.toString().contains("REGISTRATION");
    }
}


