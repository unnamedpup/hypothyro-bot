package com.github.hypothyro.bot.impl.processors.registration;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.processors.RegistrationProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("REGISTRATION_NAME")
@Slf4j
public class NameRegistrationProcessor implements RegistrationProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;


    @Override
    public SendMessage processRegistrationField(Message msg) {
        Long patientId = msg.getChatId();

        Patient patient = registrationCache.getPatientById(patientId);
        patient.setName(msg.getText());
        registrationCache.savePatient(patient);

        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Дата рождения (в формате ДД.ММ.ГГГГ):");

        log.info("RegistrationNameProcessor");

        stateCache.setState(patientId, PatientState.REGISTRATION_DOB);

        return toSend;
    }
}
