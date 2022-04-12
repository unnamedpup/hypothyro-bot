package com.github.hypothyro.bot.impl.processors.control;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.processors.ControlProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("AWAY_CHANGE_TREATMENT_MKG")
@Slf4j
public class ControlChangeTreatmentMkgCallbackProcessor implements ControlProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private PatientRepository repository;
    @Autowired private RegistrationCache registrationCache;

    public SendMessage processRegistrationField(Message msg) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(msg.getChatId().toString());
        String text = msg.getText();
        double treatmentMkg = -1;

        try {
            treatmentMkg = Double.parseDouble(text);
        } catch(NumberFormatException e) {
            log.error("Wrong treatment mkg format: {}", text);
            toSend.setText("Неверный формат данных. Пришлите число");
            return toSend;
        } 

        Patient patient = registrationCache.getPatientById(msg.getChatId());
        stateCache.setState(msg.getChatId(), PatientState.AWAY);

        if (patient == null) {
            toSend.setText("Вас нет в базе");
            return toSend;
        }

        patient.setTreatment(treatmentMkg);
        patient.setCheckinterval(2.0);
        repository.save(patient);
        registrationCache.deletePatientById(msg.getChatId());
        toSend.setText("Данные сохранены");

        return toSend;
    }
}




