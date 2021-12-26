package com.github.hypothyro.bot.impl.qualifiers;

import com.github.hypothyro.bot.handlers.UpdateHandler;
import com.github.hypothyro.bot.qualifiers.UpdateQualifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RegistrationCallbackQueryUpdateQualifier implements UpdateQualifier {

    @Autowired
    private UpdateHandler registrationCallbackQueryUpdateHandler;

    @Override
    public UpdateHandler qualify(Update update) {
        if (update.hasCallbackQuery() && isRegistrationCallback(update)) {            
            return registrationCallbackQueryUpdateHandler;
        } 
        return null;
    }

    private boolean isRegistrationCallback(Update update) {
        return update.getCallbackQuery().toString().contains("REGISTRATION");
    }
    
}

