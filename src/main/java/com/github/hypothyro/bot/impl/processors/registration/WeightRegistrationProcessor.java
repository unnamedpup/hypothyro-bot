package com.github.hypothyro.bot.impl.processors.registration;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

// FIXME: Decompose
@Service("REGISTRATION_WEIGHT")
@Slf4j
public class WeightRegistrationProcessor implements RegistrationProcessor{

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationKeyboards keyboards;

    @Override
    public SendMessage processRegistrationField(Message msg) {
        Long patientId = msg.getChatId();

        Patient patient = registrationCache.getPatientById(patientId);
        boolean canBePregnant = canBePregnant(patient);
        try {
            patient.setWeight(Integer.parseInt(msg.getText()));
        } catch(NumberFormatException e) {
            return error(patientId);
        }
        patient.setCanBePregnant(canBePregnant);
        registrationCache.savePatient(patient);

        log.info("RegistrationWeightProcessor");

        return canBePregnant
            ? askAboutPregnant(patient)
            : continueRegistration(patient);
    }


    private boolean canBePregnant(Patient patient) {
        int age = patient.getAge();
        return patient.getGender() == 1 && age >= 15 && age <= 45;
    }

    private SendMessage askAboutPregnant(Patient patient) {
        stateCache.setState(patient.getId(), PatientState.REGISTRATION_PREGNANT);

        return SendMessage.builder()
            .text("?????????????????? ???? ?????")
            .replyMarkup(keyboards.getPregnantRegistrationKeyboard())
            .chatId(patient.getId().toString())
            .build();
    }

    private SendMessage continueRegistration(Patient patient) {
        return SendMessage.builder()
            .text("???? ???????????????? ?????????????????????? ???? ?????????????????? ???????????????? ? (???????????????? ???? ????????????)")
            .replyMarkup(keyboards.getDrugRegistrationKeyboard())
            .chatId(patient.getId().toString())
            .build();
    }

    private SendMessage error(long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(Long.toString(patientId));
        toSend.setText("???????????????? ???????????? ????????????. ???????????????? ?????????? ??????????");
        return toSend;
    }
}

