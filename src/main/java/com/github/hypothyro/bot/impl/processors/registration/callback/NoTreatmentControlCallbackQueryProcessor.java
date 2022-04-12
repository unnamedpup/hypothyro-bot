package com.github.hypothyro.bot.impl.processors.registration.callback;

import java.time.Instant;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.config.RegistrationConfig;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.Notification;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.NotificationRepository;
import com.github.hypothyro.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;


@Service("REGISTRATION_PATIENT_TTG")
public class NoTreatmentControlCallbackQueryProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private PatientRepository repository;
    @Autowired private RegistrationConfig config;
    @Autowired private NotificationRepository repositoryNot;

    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        Long patientId = callback.getMessage().getChatId();

        String ttgAnswer = callback.getData().replace("REGISTRATION_PATIENT_TTG_", "");
        boolean isTtg = ttgAnswer.equals("YES");

        Patient patient = registrationCache.getPatientById(patientId);

        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());

        if (isTtg) {
            toSend.setText(config.zeroTreatmentTtgSecondAnswer1);
            if (patient.getThsResult() > 10) {
                patient.setCheckinterval(1.5);
            } else {
                patient.setCheckinterval(2.0);
            }
        } else {
            toSend.setText(config.zeroTreatmentTtgSecondAnswer2);
            patient.setCheckinterval(2.0);
        }

        // End of registration
        repository.save(patient);
        registrationCache.savePatient(patient);
        stateCache.setState(patientId, PatientState.AWAY);
        registrationCache.deletePatientById(patientId);

        repositoryNot.save(
            Notification.builder()
                .patientId(patientId)
                .text("После назначения лекарственной терапии или изменении дозировки контроль обычно осуществляется через 4-8 недель, найдите время и сдайте ТТГ (тиреотропный гормон)")
                .date(Instant.now().getEpochSecond() + 3)
                .build()
        );

        return toSend;
    }
}

