package com.github.hypothyro.bot.impl.processors.registration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

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

@Service("REGISTRATION_WEIGHT")
@Slf4j
public class WeightRegistrationProcessor implements RegistrationProcessor{

    @Autowired
    private StateMachineCache stateCache;

    @Autowired
    private RegistrationCache registrationCache;

    @Autowired
    private RegistrationKeyboards keyboards;

    @Override
    public SendMessage processRegistrationField(Message msg) {
        Long patientId = msg.getChatId();

        Patient patient = registrationCache.getPatientById(patientId);
        boolean canBePregnant = canBePregnant(patient);
        patient.setWeight(Integer.parseInt(msg.getText()));
        patient.setCanBePregnant(canBePregnant);
        registrationCache.savePatient(patient);

        log.info("RegistrationWeightProcessor");

        return canBePregnant
            ? askAboutPregnant(patient)
            : continueRegistration(patient);
    }


    private boolean canBePregnant(Patient patient) {
        int age = countAge(patient);
        return patient.getGender() == 1 && age >= 15 && age <= 45;
    }

    private SendMessage askAboutPregnant(Patient patient) {
        stateCache.setState(patient.getId(), PatientState.REGISTRATION_PREGNANT);

        return SendMessage.builder()
            .text("Беременны ли вы?")
            .replyMarkup(keyboards.getPregnantRegistrationKeyboard())
            .chatId(patient.getId().toString())
            .build();
    }

    private SendMessage continueRegistration(Patient patient) {
        stateCache.setState(patient.getId(), PatientState.REGISTRATION_TREATMENT_MKG);

        return SendMessage.builder()
            .text("До операции приходилось ли принимать тироксин ? (Выберите из списка)")
            .replyMarkup(keyboards.getDrugRegistrationKeyboard())
            .chatId(patient.getId().toString())
            .build();
    }


    private int countAge(Patient patient) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate date = LocalDate.now();

        int yearNow = date.atStartOfDay(zoneId).getYear();
        int yearDob = LocalDateTime.ofEpochSecond(patient.getDateOfBirthday(), 0, ZoneOffset.UTC).getYear();

        return yearNow - yearDob;
    }
}
