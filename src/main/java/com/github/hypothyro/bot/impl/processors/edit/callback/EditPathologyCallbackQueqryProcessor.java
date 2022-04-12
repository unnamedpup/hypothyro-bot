package com.github.hypothyro.bot.impl.processors.edit.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.config.RegistrationConfig;
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


@Service("EDIT_PATHOLOGY")
@Slf4j
public class EditPathologyCallbackQueqryProcessor implements EditCallbackProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private EditKeyboard keyboards;
    @Autowired private RegistrationConfig config;

    @Override
    public SendMessage processEditCallback(CallbackQuery callback) {
        Message msg = callback.getMessage();
        Long patientId = msg.getChatId();
        Patient patient = registrationCache.getPatientById(patientId);
        String callBackData = callback.getData();

        String pathologyIndex = callBackData.replace("EDIT_PATHOLOGY_", "");
        String pathologyName = config.pathologies.get(pathologyIndex);

        if (pathologyIndex.equals(config.changeUpthslevFieldIndex)) {
            patient.setUpthslev(2.0);
        }

        patient.setPathologyName(pathologyName);
        registrationCache.savePatient(patient);

        return sendMessage(patientId);
    }

    private SendMessage sendMessage(Long patientId) {
        stateCache.setState(patientId, PatientState.EDIT_MENU);
        SendMessage toSend = new SendMessage();
        toSend.setReplyMarkup(keyboards.constructEditKeyboard(registrationCache.getPatientById(patientId)));
        toSend.setChatId(patientId.toString());
        toSend.setText("Данные изменены");

        return toSend;
    }


}
