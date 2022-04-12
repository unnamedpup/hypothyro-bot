package com.github.hypothyro.bot.impl.processors.edit.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.edit.EditKeyboard;
import com.github.hypothyro.bot.processors.EditCallbackProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import lombok.extern.slf4j.Slf4j;

@Service("EDIT_OP")
@Slf4j
public class OperationEditCallbackQueryProcessor implements EditCallbackProcessor {

    @Autowired private RegistrationCache registrationCache;
    @Autowired private EditKeyboard keyboards;
    @Autowired private StateMachineCache stateCache;

    @Override
    public SendMessage processEditCallback(CallbackQuery callback) {
        Patient patient = registrationCache.getPatientById(callback.getMessage().getChatId());

        String operation = callback.getData().replace("REGISTRATION_OP_", "").toLowerCase();

        patient.setOperation(operation);
        registrationCache.savePatient(patient);

        return sendPathologyRequest(patient);
    }

    private SendMessage sendPathologyRequest(Patient patient) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patient.getId().toString());
        toSend.setText("Данные изменены");

        stateCache.setState(patient.getId(), PatientState.EDIT_MENU);

        toSend.setReplyMarkup(keyboards.constructEditKeyboard(patient));

        return toSend;
    }
}
