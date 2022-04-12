package com.github.hypothyro.bot.impl.qualifiers;

import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.handlers.UpdateHandler;
import com.github.hypothyro.bot.qualifiers.UpdateQualifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class ControlCallbackQualifier implements UpdateQualifier {

    @Autowired private UpdateHandler controlCallbackHandler;
    @Autowired private StateMachineCache stateCache;

    @Override
    public UpdateHandler qualify(Update update) {
        if (update.hasCallbackQuery() && isRegistrationCallback(update)) {            
            return controlCallbackHandler;
        } 
        return null;
    }

    private boolean isRegistrationCallback(Update update) {
        return stateCache.getStateById(
                update.getCallbackQuery().getMessage().getChatId()
        ).toString().contains("AWAY");
    }
    
}


