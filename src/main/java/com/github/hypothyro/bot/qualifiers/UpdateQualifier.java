package com.github.hypothyro.bot.qualifiers;

import com.github.hypothyro.bot.handlers.UpdateHandler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateQualifier {
    UpdateHandler qualify(Update update);
}
