package com.github.hypothyro.bot.impl.processors.registration.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.config.RegistrationConfig;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service("REGISTRATION_TTG")
public class ThsCallbackQueryProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationConfig config;
    @Autowired private PatientRepository repository;


    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        Message msg = callback.getMessage();
        Long patientId = msg.getChatId();
        String callBackData = callback.getData();

        String ttgAnswer = callBackData.replace("REGISTRATION_TTG_", "");
        boolean isTtg = ttgAnswer.equals("YES");

        Patient patient = registrationCache.getPatientById(patientId);

        if (isTtg) {
            return getThs(patient);
        }

        // End of registration
        registrationCache.deletePatientById(patientId);
        repository.save(patient);
        return notifyForTtg(patient);
    }

    private SendMessage getThs(Patient patient) {
        stateCache.setState(patient.getId(), PatientState.REGISTRATION_TTG_DATE);
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patient.getId().toString());
        toSend.setText(config.thsDate);

        return toSend;
    }

    private SendMessage notifyForTtg(Patient patient) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patient.getId().toString());
        boolean hasPretreatment = patient.getPretreatment() != 0;
        String endOfMsg = hasPretreatment ? config.msgYesPretreatment : config.msgNoPretreatment;
        toSend.setText(config.noTtg + " " + endOfMsg);

        return toSend;
    }
}
