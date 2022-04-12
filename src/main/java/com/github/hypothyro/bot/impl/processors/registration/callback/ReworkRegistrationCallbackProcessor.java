package com.github.hypothyro.bot.impl.processors.registration.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.edit.EditKeyboard;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service("REGISTRATION_REWORK")
public class ReworkRegistrationCallbackProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private EditKeyboard editKeyboard;
    @Autowired private RegistrationCache cache;

    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        long patientId = callback.getMessage().getChatId();

        SendMessage toSend = new SendMessage();
        toSend.setChatId(Long.toString(patientId));
        toSend.setText("Выберите данные для изменения");
        toSend.setReplyMarkup(editKeyboard.constructEditKeyboard(cache.getPatientById(patientId)));

        stateCache.setState(patientId, PatientState.EDIT_MENU);

        return toSend;
    }
}


