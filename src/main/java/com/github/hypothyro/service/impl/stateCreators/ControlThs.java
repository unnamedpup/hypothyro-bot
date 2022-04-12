package com.github.hypothyro.service.impl.stateCreators;

import java.util.Optional;

import com.github.hypothyro.bot.HypothyroBot;
import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.config.RegistrationConfig;
import com.github.hypothyro.domain.Notification;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.PatientRepository;
import com.github.hypothyro.service.NotificationStateCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service("THS_CONTROL")
@Slf4j
public class ControlThs implements NotificationStateCreator {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationConfig config;
    @Autowired private HypothyroBot bot;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private PatientRepository repository;

    @SneakyThrows
    public void createState(Notification n) {
        long id = n.getPatientId();

        Optional<Patient> patient = repository.findById(id);
        if (patient.isEmpty()) {
            log.info("There is no patient data in db");
            bot.execute(error(id));
            return;
        }

        registrationCache.savePatient(patient.get());
        stateCache.setState(id, PatientState.REGISTRATION_TTG_RESULT);

        bot.execute(ok(id));
    }

    private SendMessage ok(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText(config.thsRes);

        return toSend;
    }

    private SendMessage error(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Пользователь с вашем ID не найден");

        return toSend;
    }
}

