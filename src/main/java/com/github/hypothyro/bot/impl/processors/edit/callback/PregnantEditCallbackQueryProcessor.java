package com.github.hypothyro.bot.impl.processors.edit.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.edit.EditKeyboard;
import com.github.hypothyro.bot.processors.EditCallbackProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service("EDIT_PREGNANT")
public class PregnantEditCallbackQueryProcessor implements EditCallbackProcessor {

    @Autowired private PatientRepository patientRepository;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private StateMachineCache stateCache;
    @Autowired private EditKeyboard keyboards;

    @Override
    public SendMessage processEditCallback(CallbackQuery callback) {
        Patient patient = registrationCache.getPatientById(callback.getMessage().getChatId());

        return (callback.getData().contains("Y"))
            ? stopRegistration(patient)
            : ok(patient);
    }


    private SendMessage stopRegistration(Patient patient) {
        patient.setIsPregnant(true);
        patientRepository.save(patient);
        registrationCache.deletePatientById(patient.getId());
        stateCache.setState(patient.getId(), PatientState.AWAY);

        return SendMessage.builder()
            .text("На данный момент ведение беременности не поддерживается. Спасибо, до свидания.")
            .chatId(patient.getId().toString())
            .build();
            
    }

    private SendMessage ok(Patient patient) {
        stateCache.setState(patient.getId(), PatientState.EDIT_MENU);

        return SendMessage.builder()
            .chatId(patient.getId().toString())
            .text("Данные изменены")
            .replyMarkup(keyboards.constructEditKeyboard(patient))
            .build();

    }
}
