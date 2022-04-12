package com.github.hypothyro.bot.impl.processors.edit.callback.menu;

import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.processors.EditCallbackProcessor;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service("EDIT_WEIGHT_MENU")
public class EditWeightCallbackProcessor implements EditCallbackProcessor {

    @Autowired private StateMachineCache stateCache;

    @Override
    public SendMessage processEditCallback(CallbackQuery callback) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(callback.getMessage().getChatId().toString());

        toSend.setText("Введите ваш вес (в кг):");
        stateCache.setState(callback.getMessage().getChatId(), PatientState.EDIT_WEIGHT);

        return toSend;
    }
}
