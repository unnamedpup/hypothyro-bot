package com.github.hypothyro.bot.impl.processors.control;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.processors.ControlCallbackProcessor;
import com.github.hypothyro.domain.Notification;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service("AWAY_CHANGE_TREATMENT_NAME")
public class ControlChangeTreatmentCallbackProcessor implements ControlCallbackProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache patientsCache;
    @Autowired private NotificationRepository notificationRepository;

    private final int WEEK_IN_SECONDS = 60 * 60 * 24 * 7;
    private final int MONTH_IN_SECONDS = WEEK_IN_SECONDS * 4;

    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        long patientId = callback.getMessage().getChatId();

        SendMessage toSend = new SendMessage();
        toSend.setChatId(Long.toString(patientId));

        Patient patient = patientsCache.getPatientById(patientId);
        if (patient == null) {
            toSend.setText("Вас нет в базе");
            stateCache.setState(patientId, PatientState.AWAY);
            return toSend;
        }

        toSend.setText("Введите дозу после операции (в мкг):");

        String drugName = callback.getData().replace("CONTROL_PRETREATMENT_NAME_", "").toLowerCase();
        patient.setPretreatmentDrug(drugName);

        stateCache.setState(patientId, PatientState.AWAY_CHANGE_TREATMENT_MKG);
        patientsCache.savePatient(patient);

        notificationRepository.deleteByPatientId(patientId);
        Set<Notification> notifications = new HashSet<>();
        Map<Integer, String> times = Map.of(
                0, "После назначения лекарственной терапии или изменении дозировки контроль обычно осуществляется через 4-8 недель, найдите время и сдайте ТТГ (тиреотропный гормон)",
                1, "Уже прошло 5 недель с момента смены терапии.",
                2, "Уже прошло 6 недель с момента смены терапии.",
                3, "Если вы еще не сдали ТТГ, то поспешите, рекомендованный интервал заканчивается через неделю.",
                4, "Прошу вас не игнорируйте контроль гормонов, даже если вы себя хорошо чувствуете, это еще не значит, что терапия адекватная",
                5, "Прошу вас не игнорируйте контроль гормонов, даже если вы себя хорошо чувствуете, это еще не значит, что терапия адекватная",
                6, "Прошу вас не игнорируйте контроль гормонов, даже если вы себя хорошо чувствуете, это еще не значит, что терапия адекватная",
                7, "Прошу вас не игнорируйте контроль гормонов, даже если вы себя хорошо чувствуете, это еще не значит, что терапия адекватная"
        );


        for (Entry<Integer, String> i : times.entrySet()) {
            long date = Instant.now().getEpochSecond() + MONTH_IN_SECONDS + WEEK_IN_SECONDS * i.getKey();
            if (Instant.now().getEpochSecond() < date) {
                Notification notification = Notification.builder()
                    .patientId(patientId)
                    .text(i.getValue())
                    .date(date)
                    .build();
                notifications.add(notification);
            }
        }
        notificationRepository.saveAll(notifications);

        return toSend;
    }
}


