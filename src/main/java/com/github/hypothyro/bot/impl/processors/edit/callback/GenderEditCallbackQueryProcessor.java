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

@Service("EDIT_GENDER")
@Slf4j
public class GenderEditCallbackQueryProcessor implements EditCallbackProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private EditKeyboard keyboards;


    @Override
    public SendMessage processEditCallback(CallbackQuery callback) {
        Long patientId = callback.getMessage().getChatId();
        Patient patient = registrationCache.getPatientById(patientId);
        int gender = processGender(callback.getData());

        patient.setGender(gender);
        registrationCache.savePatient(patient);
        stateCache.setState(patientId, PatientState.EDIT_MENU);

        return prepareMessage(patientId);
    }


    private int processGender(String callBackData) {
        if (callBackData.contains("FEMALE")){
            return 1;
        } else if (callBackData.contains("MALE")){
            return 0;
        } else {
            return 2;
        }
    }

    private SendMessage prepareMessage(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Данные изменены");
        toSend.setReplyMarkup(keyboards.constructEditKeyboard(registrationCache.getPatientById(patientId)));
        return toSend;
    }
}

