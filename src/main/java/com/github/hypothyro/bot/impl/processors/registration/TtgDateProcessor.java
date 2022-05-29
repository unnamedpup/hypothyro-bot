package com.github.hypothyro.bot.impl.processors.registration;

import java.time.format.DateTimeParseException;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.config.RegistrationConfig;
import com.github.hypothyro.bot.keyboards.control.ControlKeyboards;
import com.github.hypothyro.bot.processors.RegistrationProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.PatientRepository;
import com.github.hypothyro.service.impl.DefaultDateChecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

// FIXME: Fix tooOld method
@Service("REGISTRATION_TTG_DATE")
@Slf4j
public class TtgDateProcessor implements RegistrationProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationConfig config;
    @Autowired private PatientRepository repository;
    @Autowired private ControlKeyboards controlKeyboards;


    @Override
    public SendMessage processRegistrationField(Message msg) {
        Long patientId = msg.getChatId();

        try {
            Patient patient = registrationCache.getPatientById(patientId);
            DefaultDateChecker date_checker = new DefaultDateChecker(msg.getText());
            long thsDate = date_checker.getDate();

            patient.setThsDate(thsDate);
            repository.save(patient);
            registrationCache.savePatient(patient);

            if (date_checker.isEarlierThen2Months()) {
                return ok(patientId);
            } else {
                return tooOld(patientId);
            }


        } catch (DateTimeParseException e) {
            return error(patientId);
        }

    }

    private SendMessage ok(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText(config.thsRes);
        stateCache.setState(patientId, PatientState.REGISTRATION_TTG_RESULT);

        return toSend;
    }


    private SendMessage error(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Неправильный формат данных.(введите в формате дд.мм.гггг)");

        return toSend;
    }

    private SendMessage tooOld(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Нужно сдать новый анализ");
        toSend.setReplyMarkup(controlKeyboards.controlButtons);
        stateCache.setState(patientId, PatientState.AWAY);

        return toSend;
    }

}
