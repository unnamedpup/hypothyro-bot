package com.github.hypothyro.bot.impl.processors.registration.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import lombok.extern.slf4j.Slf4j;

@Service("REGISTRATION_GENDER")
@Slf4j
public class GenderRegistrationCallbackQueryProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired
    private StateMachineCache stateCache;

    @Autowired
    private RegistrationCache registrationCache;


    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        log.info("RegistrationGenderProcessor");

        Long patientId = callback.getMessage().getChatId();

        Patient patient = registrationCache.getPatientById(patientId);
        int gender = processGender(callback.getData());
        patient.setGender(gender);

        registrationCache.savePatient(patient);
        stateCache.setState(patientId, PatientState.REGISTRATION_WEIGHT);

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
        toSend.setText("Сколько вы весите? (В килограммах)");
        return toSend;
    }
}

