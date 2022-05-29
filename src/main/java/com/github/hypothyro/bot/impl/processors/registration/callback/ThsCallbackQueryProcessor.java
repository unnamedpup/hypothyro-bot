package com.github.hypothyro.bot.impl.processors.registration.callback;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.config.RegistrationConfig;
import com.github.hypothyro.bot.keyboards.control.ControlKeyboards;
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
import org.telegram.telegrambots.meta.api.objects.Message;

@Service("REGISTRATION_TTG")
public class ThsCallbackQueryProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationConfig config;
    @Autowired private PatientRepository repository;
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private ControlKeyboards controlKeyboards;

    private final int WEEK_IN_SECONDS = 60 * 60 * 24 * 7;
    private final int MONTH_IN_SECONDS = WEEK_IN_SECONDS * 4;


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
        stateCache.setState(patientId, PatientState.AWAY);


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
            long date = patient.getDateOperation() + MONTH_IN_SECONDS + WEEK_IN_SECONDS * i.getKey();
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
        toSend.setReplyMarkup(controlKeyboards.controlButtons);

        return toSend;
    }
}
