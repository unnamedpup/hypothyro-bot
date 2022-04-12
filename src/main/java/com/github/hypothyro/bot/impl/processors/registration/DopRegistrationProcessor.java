package com.github.hypothyro.bot.impl.processors.registration;

import java.time.format.DateTimeParseException;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.service.impl.DefaultDateChecker;

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

    public SendMessage processRegistrationField(Message msg) {
        Long patientId = msg.getChatId();

        try {
            Patient patient = registrationCache.getPatientById(patientId);
            long dop = new DefaultDateChecker(msg.getText()).getDate();
            patient.setDateOperation(dop);
            registrationCache.savePatient(patient);

            return ok(patientId);
        } catch (DateTimeParseException e) {
            return error(patientId);
        }

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
        toSend.setText("Неправильный формат данных.(введите в формате дд.мм.гггг)");

        return toSend;
    }
}
