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


@Service("REGISTRATION_DOP")
@Slf4j
public class DopRegistrationProcessor implements RegistrationProcessor{

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationKeyboards keyboards;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public SendMessage processRegistrationField(Message msg) {
        Long patientId = msg.getChatId();

        try {
            Patient patient = registrationCache.getPatientById(patientId);
            long dop = convertStringToUnix(msg.getText());
            patient.setDateOperation(dop);
            registrationCache.savePatient(patient);

            return ok(patientId);
        } catch (DateTimeParseException e) {
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
        toSend.setText("Щитовидная железа была удалена вся ?");
        toSend.setReplyMarkup(keyboards.getOperationRegistrationKeyboard());

        log.info("RegistrationDOPProcessor");
        stateCache.setState(patientId, PatientState.REGISTRATION_OP);

        return toSend;
    }


    private SendMessage error(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Wrong date format! Must be dd.mm.yyyy");

        return toSend;
    }
}
