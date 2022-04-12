package com.github.hypothyro.bot;

import java.util.List;

import com.github.hypothyro.bot.qualifiers.UpdateQualifier;
import com.github.hypothyro.bot.handlers.UpdateHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Dispatcher {

    @Autowired List<UpdateQualifier> updateQualifiers;

    private UpdateHandler errorHandler = new UpdateHandler() {
        @Override
        public SendMessage handle(Update update) {
            Long chatId = update.getMessage() == null 
                ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();

            return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Неизвестная ошибка.\nОбратитесь за помощью по нику @unnamedpup")
                .build();
        }
    };

    @SneakyThrows
    public SendMessage dispatch(Update update) {
        log.info("Update in dispathcer {}", update);
        return updateQualifiers.stream()
            .map(qualifier -> qualifier.qualify(update))
            .filter(handler -> handler != null)
            .findFirst()
            .orElseGet(() -> errorHandler)
            .handle(update);
    }
}
