package com.github.hypothyro.bot.impl.processors.registration.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.Patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import lombok.extern.slf4j.Slf4j;

@Service("REGISTRATION_OP")
@Slf4j
public class OPRegistrationCallbackQueryProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationKeyboards keyboards;

    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        Patient patient = registrationCache.getPatientById(callback.getMessage().getChatId());

        String operation = callback.getData().replace("REGISTRATION_OP_", "").toLowerCase();

        patient.setOperation(operation);
        registrationCache.savePatient(patient);

        log.info("RegistrationOPProcessor");

        return sendPathologyRequest(patient);
    }

    private SendMessage sendPathologyRequest(Patient patient) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patient.getId().toString());
        toSend.setText("По гистологии пришло:");
        toSend.setReplyMarkup(keyboards.getPathologyKeyboard());

        return toSend;
    }
}
