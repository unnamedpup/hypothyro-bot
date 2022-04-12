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


@Service("EDIT_DOP")
@Slf4j
public class DopEditProcessor implements EditMessageProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private EditKeyboard keyboards;

    public SendMessage processEditMessage(Message msg) {
        Long patientId = msg.getChatId();
        Patient patient = registrationCache.getPatientById(patientId);
        long dop;

        try {
            dop = new DefaultDateChecker(msg.getText()).getDate();
        } catch (DateTimeParseException e) {
            return error(patientId);
        }

        patient.setDateOperation(dop);
        registrationCache.savePatient(patient);

        return ok(patientId);
    }

    private SendMessage ok(Long patientId) {

        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Данные изменены");
        toSend.setReplyMarkup(keyboards.constructEditKeyboard(registrationCache.getPatientById(patientId)));

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
