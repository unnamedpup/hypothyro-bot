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

@Service("EDIT_NAME")
public class NameEditProcessor implements EditMessageProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private EditKeyboard keyboards;


    @Override
    public SendMessage processEditMessage(Message msg) {
        Long patientId = msg.getChatId();

        Patient patient = registrationCache.getPatientById(patientId);
        patient.setName(msg.getText());

        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Данные изменены");
        toSend.setReplyMarkup(keyboards.constructEditKeyboard(patient));

        stateCache.setState(patientId, PatientState.EDIT_MENU);
        registrationCache.savePatient(patient);

        return toSend;
    }
}
