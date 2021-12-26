package com.github.hypothyro.bot.impl.qualifiers;

import com.github.hypothyro.bot.handlers.UpdateHandler;
import com.github.hypothyro.bot.qualifiers.UpdateQualifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class CommandUpdateQualifier implements UpdateQualifier {

    @Autowired
    private UpdateHandler commandUpdateHandler;

    public UpdateHandler qualify(Update update) {
        if (update.hasMessage() && isCommand(update.getMessage())) {
            return commandUpdateHandler;
        }

        return null;
    }

    
    private boolean isCommand(Message message) {
        return message.getText().startsWith("/");
    }
}

