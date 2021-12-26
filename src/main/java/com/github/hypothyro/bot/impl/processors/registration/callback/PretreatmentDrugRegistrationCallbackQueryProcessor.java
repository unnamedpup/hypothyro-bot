package com.github.hypothyro.bot.impl.processors.registration.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.bot.processors.RegistrationProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("REGISTRATION_PRETREATMENT_NAME")
@Slf4j
public class PretreatmentDrugRegistrationCallbackQueryProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;

    @Autowired private RegistrationProcessor REGISTRATION_PRETREATMENT_MKG;

    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        Message msg = callback.getMessage();
        Long patientId = msg.getChatId();
        Patient patient = registrationCache.getPatientById(patientId);
        String callBackData = callback.getData();
        String drugName = callBackData.replace("REGISTRATION_PRETREATMENT_NAME_", "").toLowerCase();

        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());

        if (patient.getDateOperation() == null) {
            if (drugName.equals("nothing")) {
                patient.setPretreatment(0.0);
                toSend.setText("Когда была операция ?");
                stateCache.setState(patientId, PatientState.REGISTRATION_DOP);
            } else {
                patient.setPretreatmentDrug(drugName);

                toSend.setText("Введите дозу (в мкг):");
                stateCache.setState(patientId, PatientState.REGISTRATION_PRETREATMENT_MKG);

                log.info("RegistrationPretreatmentProcessor");
            }

        } else {
            if (drugName.equals("nothing")) {
                msg.setText("0");
                return REGISTRATION_PRETREATMENT_MKG.processRegistrationField(msg);
            } else {
                toSend.setText("Введите дозу (в мкг):");
                patient.setTreatmentDrug(drugName);
                stateCache.setState(patientId, PatientState.REGISTRATION_PRETREATMENT_MKG);
            }
        }

        registrationCache.savePatient(patient);

        return toSend;
    }
}

