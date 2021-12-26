package com.github.hypothyro.bot.impl.processors.registration.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service("REGISTRATION_PREGNANT")
public class PregnantRegistrationCallbackQueryProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RegistrationCache registrationCache;

    @Autowired
    private StateMachineCache stateCache;

    @Autowired
    private RegistrationKeyboards keyboards;

    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        Patient patient = registrationCache.getPatientById(callback.getMessage().getChatId());

        return (callback.getData().contains("Y"))
            ? stopRegistration(patient)
            : continueRegistration(patient);
    }


    private SendMessage stopRegistration(Patient patient) {
        patient.setIsPregnant(true);
        patientRepository.save(patient);
        registrationCache.deletePatientById(patient.getId());

        return SendMessage.builder()
            .text("Pregnant is not supported")
            .chatId(patient.getId().toString())
            .build();
            
    }

    private SendMessage continueRegistration(Patient patient) {
        stateCache.setState(patient.getId(), PatientState.REGISTRATION_PRETREATMENT_MKG);

        return SendMessage.builder()
            .chatId(patient.getId().toString())
            .text("До операции приходилось ли принимать Тироксин ?")
            .replyMarkup(keyboards.getDrugRegistrationKeyboard())
            .build();

    }
}
