package com.github.hypothyro.bot.impl.processors.registration.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.config.RegistrationConfig;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import lombok.extern.slf4j.Slf4j;

@Service("REGISTRATION_RAD")
@Slf4j
public class RadProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private PatientRepository repository;
    @Autowired private RegistrationConfig config;

    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        Long patientId = callback.getMessage().getChatId();

        String ttgAnswer = callback.getData().replace("REGISTRATION_RAD_", "");
        boolean isTtg = ttgAnswer.equals("YES");

        Patient patient = registrationCache.getPatientById(patientId);

        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());

        if (isTtg) {
            toSend.setText("Важным условием супрессивной терапии является безопасность. Есть ли у вас сердцебиения, потливость быстрая утомляемость, то имеет смысл снизить дозу");
        } else {
            toSend.setText("Контроль через 3 месяца.");
            patient.setCheckinterval(3.0);
        }

        // End of registration
        repository.save(patient);
        registrationCache.savePatient(patient);
        stateCache.setState(patientId, PatientState.AWAY);
        registrationCache.deletePatientById(patientId);

        return toSend;
    }
}

