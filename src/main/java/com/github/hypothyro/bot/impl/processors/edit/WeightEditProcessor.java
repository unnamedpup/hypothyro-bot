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

@Service("EDIT_WEIGHT")
public class WeightEditProcessor implements EditMessageProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private EditKeyboard keyboards;

    @Override
    public SendMessage processEditMessage(Message msg) {
        Long patientId = msg.getChatId();
        Patient patient = registrationCache.getPatientById(patientId);

        boolean canBePregnant = canBePregnant(patient);

        try {
            patient.setWeight(Integer.parseInt(msg.getText()));
        } catch(NumberFormatException e) {
            return error(patientId);
        }

        patient.setCanBePregnant(canBePregnant);
        registrationCache.savePatient(patient);

        return canBePregnant
            ? askAboutPregnant(patient)
            : continueRegistration(patient);
    }


    private boolean canBePregnant(Patient patient) {
        int age = patient.getAge();
        return patient.getGender() == 1 && age >= 15 && age <= 45;
    }

    private SendMessage askAboutPregnant(Patient patient) {
        stateCache.setState(patient.getId(), PatientState.EDIT_PREGNANT);

        return SendMessage.builder()
            .text("Беременны ли вы?")
            .replyMarkup(keyboards.editPregnantKeyboard)
            .chatId(patient.getId().toString())
            .build();
    }

    private SendMessage continueRegistration(Patient patient) {
        stateCache.setState(patient.getId(), PatientState.EDIT_MENU);

        return SendMessage.builder()
            .text("Данные изменены")
            .replyMarkup(keyboards.constructEditKeyboard(patient))
            .chatId(patient.getId().toString())
            .build();
    }

    private SendMessage error(long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(Long.toString(patientId));
        toSend.setText("Неверный формат данных. Пришлите число");
        return toSend;
    }
}

