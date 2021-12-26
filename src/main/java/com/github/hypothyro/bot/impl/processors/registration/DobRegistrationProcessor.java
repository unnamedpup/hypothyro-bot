package com.github.hypothyro.bot.impl.processors.registration;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("REGISTRATION_DOB")
@Slf4j
public class DobRegistrationProcessor implements RegistrationProcessor{

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationKeyboards keyboards;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public SendMessage processRegistrationField(Message msg) {
        log.info("RegistrationDOBProcessor");

        Long patientId = msg.getChatId();
        Patient patient = registrationCache.getPatientById(patientId);

        try {
            Long epoch = convertStringToUnix(msg.getText());
            log.info("DOB: {}", epoch);

            patient.setDateOfBirthday(epoch);
            registrationCache.savePatient(patient);

            return ok(patientId);

        } catch (DateTimeParseException e) {
            log.error("Wrong date format: {}", msg.getText());
            return error(patientId);
        }

    }


    private Long convertStringToUnix(String in) {
        LocalDate date = LocalDate.parse(in.replace("\\s", "").replace("(-)|(\\/)", "."), formatter);
        log.info("Date: {}", date);
        ZoneId zoneId = ZoneId.systemDefault();
        log.info("ZondeId: {}", zoneId);
        return date.atStartOfDay(zoneId).toEpochSecond();
    }

    private SendMessage ok(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Выберите ваш пол");
        toSend.setReplyMarkup(keyboards.getGenderRegistrationKeyboard());

        stateCache.setState(patientId, PatientState.REGISTRATION_GENDER);

        return toSend;
    }


    private SendMessage error(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Wrong date format! Must be dd.mm.yyyy");

        return toSend;
    }
}