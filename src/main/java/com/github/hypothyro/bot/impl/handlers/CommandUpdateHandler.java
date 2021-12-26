package com.github.hypothyro.bot.impl.handlers;

import com.github.hypothyro.bot.handlers.UpdateHandler;
import com.github.hypothyro.bot.processors.CommandProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Service
@Slf4j
public class CommandUpdateHandler implements UpdateHandler {

    @Autowired
    private Map<String, CommandProcessor> commandProcessorsChain;

    public SendMessage handle(Update update) {
        log.info("Handling command update !");

        Message msg = update.getMessage();
        CommandProcessor p = commandProcessorsChain.get(msg.getText().substring(1));

        if (p != null) {
            return p.processCommand(msg);
        }

        return null;
    }
}
