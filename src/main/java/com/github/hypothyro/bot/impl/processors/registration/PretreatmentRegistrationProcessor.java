package com.github.hypothyro.bot.impl.processors.registration;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.config.RegistrationConfig;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service("REGISTRATION_PRETREATMENT_MKG")
public class PretreatmentRegistrationProcessor implements RegistrationProcessor {

    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationKeyboards keyboards;
    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationConfig config;

    public SendMessage processRegistrationField(Message msg) {
        Patient patient = registrationCache.getPatientById(msg.getChatId());

        SendMessage toSend = new SendMessage();
        toSend.setChatId(patient.getId().toString());
        double drugMkg;

        try {
            drugMkg = Double.parseDouble(msg.getText());
        } catch (NumberFormatException e) {
            toSend.setText("Неверный формат данных. Пришлите число");
            return toSend;
        }

        if (patient.getDateOperation() == null) {
            patient.setPretreatment(drugMkg);
            if (patient.getPretreatment() == 0) {
                toSend.setText("Введите дозу:");
                stateCache.setState(patient.getId(), PatientState.REGISTRATION_DOP);
            } else {
                toSend.setText("Когда была операция?(В формате ДД.ММ.ГГГГ)");
                stateCache.setState(patient.getId(), PatientState.REGISTRATION_DOP);
            }
        } else {
            patient.setTreatment(drugMkg);
            toSend.setText(config.confirmRegistrationn);
            toSend.setReplyMarkup(keyboards.getVerifyRegistrationFinishKeyboard());
        }

        registrationCache.savePatient(patient);
        return toSend;
    }
}
