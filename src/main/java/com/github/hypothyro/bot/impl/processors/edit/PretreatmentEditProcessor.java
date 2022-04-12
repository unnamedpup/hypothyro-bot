package com.github.hypothyro.bot.impl.processors.edit;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.edit.EditKeyboard;
import com.github.hypothyro.bot.processors.EditMessageProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("EDIT_PRETREATMENT_MKG")
@Slf4j
public class PretreatmentEditProcessor implements EditMessageProcessor {

    @Autowired private RegistrationCache registrationCache;
    @Autowired private EditKeyboard keyboards;
    @Autowired private StateMachineCache stateCache;

    public SendMessage processEditMessage(Message msg) {
        Patient patient = registrationCache.getPatientById(msg.getChatId());

        SendMessage toSend = new SendMessage();
        toSend.setChatId(patient.getId().toString());
        double drugMkg;

        try {
            drugMkg = Double.parseDouble(msg.getText());
        } catch (NumberFormatException e) {
            toSend.setText("Неверный формат данных. Пришлите число");
            return toSend;
        }

        patient.setPretreatment(drugMkg);
        stateCache.setState(patient.getId(), PatientState.EDIT_MENU);

        toSend.setText("Данные изменены");
        toSend.setReplyMarkup(keyboards.constructEditKeyboard(patient));
        registrationCache.savePatient(patient);

        return toSend;
    }
}
