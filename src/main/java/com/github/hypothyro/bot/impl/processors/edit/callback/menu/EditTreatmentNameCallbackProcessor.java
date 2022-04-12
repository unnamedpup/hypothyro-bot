package com.github.hypothyro.bot.impl.processors.edit.callback.menu;

import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.edit.EditKeyboard;
import com.github.hypothyro.bot.processors.EditCallbackProcessor;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service("EDIT_TREATMENT_DRUG_MENU")
public class EditTreatmentNameCallbackProcessor implements EditCallbackProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private EditKeyboard keyboards;

    @Override
    public SendMessage processEditCallback(CallbackQuery callback) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(callback.getMessage().getChatId().toString());

        toSend.setText("Какой препарат принимаете после операции ?");
        toSend.setReplyMarkup(keyboards.editTreatmentName);
        stateCache.setState(callback.getMessage().getChatId(), PatientState.EDIT_TREATMENT_NAME);

        return toSend;
    }
}

