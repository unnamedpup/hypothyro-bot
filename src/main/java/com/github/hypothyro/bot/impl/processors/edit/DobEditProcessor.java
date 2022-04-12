package com.github.hypothyro.bot.impl.processors.edit;

import java.time.format.DateTimeParseException;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.edit.EditKeyboard;
import com.github.hypothyro.bot.processors.EditMessageProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.service.impl.DefaultDateChecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("EDIT_DOB")
@Slf4j
public class DobEditProcessor implements EditMessageProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private EditKeyboard keyboard;


    public SendMessage processEditMessage(Message msg) {

        Long patientId = msg.getChatId();
        Patient patient = registrationCache.getPatientById(patientId);

        try {
            Long epoch = new DefaultDateChecker(msg.getText()).getDate();
            log.info("DOB: {}", epoch);

            patient.setDateOfBirthday(epoch);
            registrationCache.savePatient(patient);

            return ok(patientId);

        } catch (DateTimeParseException e) {
            log.error("Wrong date format: {}", msg.getText());
            return error(patientId);
        }

    }


    private SendMessage ok(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Данные изменены");
        toSend.setReplyMarkup(keyboard.constructEditKeyboard(registrationCache.getPatientById(patientId)));

        stateCache.setState(patientId, PatientState.EDIT_MENU);

        return toSend;
    }


    private SendMessage error(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Неправильный формат данных.(введите в формате дд.мм.гггг)");

        return toSend;
    }
}
