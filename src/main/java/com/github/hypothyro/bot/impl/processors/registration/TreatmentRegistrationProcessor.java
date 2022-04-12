package com.github.hypothyro.bot.impl.processors.registration;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationProcessor;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service("REGISTRATION_TREATMENT_MKG")
@Slf4j
public class TreatmentRegistrationProcessor implements RegistrationProcessor{

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationKeyboards keyboards;

    public SendMessage processRegistrationField(Message msg) {
        Long patientId = msg.getChatId();
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        try {
            log.info("RegistrationTreatmentProcessor");

            Patient patient = registrationCache.getPatientById(patientId);
            patient.setTreatment(Double.parseDouble(msg.getText()));

            toSend.setReplyMarkup(keyboards.getDrugRegistrationKeyboard());
            toSend.setText("some text");
            registrationCache.savePatient(patient);
            stateCache.setState(patientId, PatientState.REGISTRATION_PRETREATMENT_NAME); // Ocenka

            return toSend;
        } catch (NumberFormatException e) {
            toSend.setText("wrong format");
            return toSend;
        }
    }
}
