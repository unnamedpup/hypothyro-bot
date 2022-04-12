package com.github.hypothyro.bot.processors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface ControlProcessor {
    SendMessage processRegistrationField(Message msg);
}


