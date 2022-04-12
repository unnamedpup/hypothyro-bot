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
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("EDIT_TREATMENT_NAME")
@Slf4j
public class TreatmentDrugEditCallbackProcessor implements EditCallbackProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private EditKeyboard keyboards;

    @Override
    public SendMessage processEditCallback(CallbackQuery callback) {
        Message msg = callback.getMessage();
        Long patientId = msg.getChatId();
        Patient patient = registrationCache.getPatientById(patientId);
        String callBackData = callback.getData();
        String drugName = callBackData.replace("EDIT_TREATMENT_NAME_", "").toLowerCase();
        patient.setTreatmentDrug(drugName);

        if (drugName.equals("nothing")) {
            patient.setTreatment(0.0);
        }

        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setReplyMarkup(keyboards.constructEditKeyboard(patient));
        toSend.setText("Данные изменены");

        stateCache.setState(patientId, PatientState.EDIT_MENU);
        registrationCache.savePatient(patient);

        return toSend;
    }
}


