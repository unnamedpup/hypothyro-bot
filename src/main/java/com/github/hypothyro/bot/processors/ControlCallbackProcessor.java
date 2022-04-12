package com.github.hypothyro.bot.processors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface ControlCallbackProcessor {
    SendMessage processRegistrationCallback(CallbackQuery callback);
}


