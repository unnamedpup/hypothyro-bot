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
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.Notification;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.NotificationRepository;
import com.github.hypothyro.repository.PatientRepository;
import com.github.hypothyro.service.impl.DefaultDateChecker;

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
    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationConfig config;
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private ControlKeyboards controlKeyboards;

    private final int WEEK_IN_SECONDS = 60 * 60 * 24 * 7;
    private final int MONTH_IN_SECONDS = WEEK_IN_SECONDS * 4;


    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        Message msg = callback.getMessage();

        Patient newPatient = registrationCache.getPatientById(msg.getChatId());
        patientRepository.save(newPatient);

        SendMessage toSend = new SendMessage();

        toSend.setChatId(msg.getChatId().toString());
        if (new DefaultDateChecker(newPatient.getDateOperation()).isEarlierThen2Months()) {
            // End of registration without THS
            registrationCache.deletePatientById(msg.getChatId());
            repository.save(newPatient);
            toSend.setText(processEvaluation(newPatient));
            toSend.setReplyMarkup(controlKeyboards.controlButtons);
            notifyPatient(newPatient);

            stateCache.setState(msg.getChatId(), PatientState.AWAY);
        } else {
            log.info("Date more then 2 month");
            toSend.setText(processEvaluation(newPatient) + "\n\nСдавали ли вы после этого контрольный анализ на ТТГ?");
            toSend.setReplyMarkup(keyboards.getTtgKeyboard());
            stateCache.setState(msg.getChatId(), PatientState.REGISTRATION_TTG_YESNO);
        }


        return toSend;
    }

    private String processEvaluation(Patient patient) {
        StringBuilder sb = new StringBuilder("Конец регистрации");

        if (patient.getOperation().equals("all")) {
            if (patient.getTreatment() == 0) {
                sb.append("\nПациенты без щитовидной железы должны получать заместительную терапию, пожалуйста обратитесь к своему врачу за разяснениями");
            } else if (isDoseNormal(patient)) {
                sb.append("\nВозможно, эта дозировка недостаточная для вас.");
            }
        }
        return sb.toString();
    }


    private boolean isDoseNormal(Patient patient) {
        return patient.getTreatment() < patient.getWeight() * 1.6 - 25;
    }

    private void notifyPatient(Patient patient) {
        notificationRepository.deleteByPatientId(patient.getId());
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
                    .patientId(patient.getId())
                    .text(i.getValue())
                    .date(date)
                    .build();
                notifications.add(notification);
            }
        }
        notificationRepository.saveAll(notifications);
    }
}

