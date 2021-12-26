package com.github.hypothyro.bot.impl.handlers;

import java.util.Map;

import com.github.hypothyro.bot.handlers.UpdateHandler;
import com.github.hypothyro.bot.keyboards.registration.RegistrationCallback;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;


@Service
@Slf4j
public class RegistrationCallbackQueryUpdateHandler implements UpdateHandler {

    @Autowired
    private Map<String, RegistrationCallbackQueryProcessor> registrationCallbackProcessorCahin;

    @Override
    public SendMessage handle(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        SendMessage toSend = registrationCallbackProcessorCahin
            .get(RegistrationCallback.valueOf(callbackQuery.getData()).getPrfix())
            .processRegistrationCallback(callbackQuery);

        return toSend;
    }
}
