package com.github.hypothyro.bot.impl.processors.registration.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.config.RegistrationConfig;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;


@Service("REGISTRATION_PATHOLOGY")
@Slf4j
public class PathologyCallbackQueqryProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationKeyboards keyboards;
    @Autowired private RegistrationConfig config;

    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        Message msg = callback.getMessage();
        Long patientId = msg.getChatId();
        Patient patient = registrationCache.getPatientById(patientId);
        String callBackData = callback.getData();

        String pathologyIndex = callBackData.replace("REGISTRATION_PATHOLOGY_", "");
        String pathologyName = config.pathologies.get(pathologyIndex);

        if (pathologyIndex.equals(config.changeUpthslevFieldIndex)) {
            patient.setUpthslev(2.0);
        }

        patient.setPathologyName(pathologyName);
        registrationCache.savePatient(patient);

        return sendMessage(patientId);
    }

    private SendMessage sendMessage(Long patientId) {
        stateCache.setState(patientId, PatientState.REGISTRATION_PRETREATMENT_NAME);
        SendMessage toSend = new SendMessage();
        toSend.setReplyMarkup(keyboards.getDrugRegistrationKeyboard());
        toSend.setChatId(patientId.toString());
        toSend.setText("Какой препарат был назначен после операции ?");

        return toSend;
    }


}
