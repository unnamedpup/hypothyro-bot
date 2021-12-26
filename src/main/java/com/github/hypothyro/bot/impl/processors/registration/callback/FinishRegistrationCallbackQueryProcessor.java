package com.github.hypothyro.bot.impl.processors.registration.callback;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("REGISTRATION_FINISH")
@Slf4j
public class FinishRegistrationCallbackQueryProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired private PatientRepository patientRepository;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationKeyboards keyboards;
    @Autowired private PatientRepository repository;

    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        Message msg = callback.getMessage();

        Patient newPatient = registrationCache.getPatientById(msg.getChatId());
        boolean hasNotThs = newPatient.getThsResult() == null;
        patientRepository.save(newPatient);

        SendMessage toSend = new SendMessage();

        toSend.setChatId(msg.getChatId().toString());
        if (hasNotThs && is2MothPassedFromDop(newPatient)) {
            log.info("Date more then 2 month");
            toSend.setText(processEvaluation(newPatient) + "\n\nСдавали ли вы после этого контрольный анализ на ТТГ?");
            toSend.setReplyMarkup(keyboards.getTtgKeyboard());
        } else {
            // End of registration without THS
            registrationCache.deletePatientById(msg.getChatId());
            repository.save(newPatient);
            toSend.setText("Registration confirmed!\n\n" + processEvaluation(newPatient));
        }

        return toSend;
    }

    private String processEvaluation(Patient patient) {
        StringBuilder sb = new StringBuilder();

        if (patient.getOperation().equals("all")) {
            if (patient.getTreatment() == 0) {
                sb.append("Пациенты без щитовидной железы должны получать заместительную терапию, пожалуйста обратитесь к своему врачу за разяснениями");
            } else if (isDoseNormal(patient)) {
                sb.append("Возможно, эта дозировка недостаточная для вас.");
            }
        }

        return sb.toString();
    }


    private boolean isDoseNormal(Patient patient) {
        return patient.getTreatment() < patient.getWeight() * 1.6 - 25;
    }

    private boolean is2MothPassedFromDop(Patient patient) {
        Instant dopInstant = Instant.ofEpochSecond(patient.getDateOperation());
        Period res = Period.between(LocalDate.now(), LocalDate.ofInstant(dopInstant, ZoneId.systemDefault()));
        double resHui = Math.abs(res.getYears() * 12 + res.getMonths() + res.getDays() / 30.0);
        log.info("DURATION RES: {}, years: {}", res, resHui);
        // return res.getYears() >= 0 && res.getMonths() > 2 && res.getDays() > 0;
        return resHui > 2.0;
    }
}
