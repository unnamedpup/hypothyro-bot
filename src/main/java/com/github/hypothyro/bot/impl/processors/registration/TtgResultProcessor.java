package com.github.hypothyro.bot.impl.processors.registration;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.config.RegistrationConfig;
import com.github.hypothyro.bot.keyboards.control.ControlKeyboards;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.RegistrationProcessor;
import com.github.hypothyro.domain.Notification;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.NotificationRepository;
import com.github.hypothyro.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;


@Service("REGISTRATION_TTG_RESULT")
@Slf4j
public class TtgResultProcessor implements RegistrationProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;
    @Autowired private RegistrationConfig config;
    @Autowired private PatientRepository repository;
    @Autowired private RegistrationKeyboards keyboards;
    @Autowired private ControlKeyboards controlKeyboards;
    @Autowired private NotificationRepository notificationRepository;

    private final int WEEK_IN_SECONDS = 60 * 60 * 24 * 7;
    private final int MONTH_IN_SECONDS = WEEK_IN_SECONDS * 4;

    @Override
    public SendMessage processRegistrationField(Message msg) {
        Long patientId = msg.getChatId();

        // End of registration
        try {
            Patient patient = registrationCache.getPatientById(patientId);
            patient.setThsResult(Double.parseDouble(msg.getText()));

            SendMessage toSend = new SendMessage();
            toSend.setChatId(patientId.toString());
            StringBuilder sb = new StringBuilder();
            double ttg = patient.getThsResult();

            if (patient.getTreatment() == 0.0) {
                if (ttg < patient.getLowthslev()) {
                    toSend.setText(config.zeroTtgFirst);
                } else if (ttg > patient.getUpthslev()) {
                    sb.append(config.zeroTreatmentTtgSecond1);
                    if (ttg < 6) {
                        log.info("ttg < 6");
                        sb.append(config.zeroTreatmentTtgSecond21);
                        if (patient.getCanBePregnant()) {
                            sb.append(config.ttgWannaBePregnant);
                        }
                        toSend.setText(sb.toString());
                        toSend.setReplyMarkup(keyboards.getPatientTtgKeyboard());
                        return toSend;

                    } else if (ttg > 6) {
                        log.info("ttg > 6");
                        sb.append(config.zeroTreatmentTtgSecond3);
                        if (ttg > 10) {
                            patient.setCheckinterval(1.5);
                        } else {
                            patient.setCheckinterval(2.0);
                        }
                        toSend.setText(sb.toString());
                    } else {
                        if (patient.getCheckinterval() > 6 && patient.getCheckinterval() < 12) {
                            patient.setCheckinterval(12.0);
                            toSend.setText(config.ttgThird + "1 год");
                        }
                        else {
                            patient.setCheckinterval(6.0);
                            toSend.setText(config.ttgThird + "6 месяцев");
                        }
                    }
                } else {
                    if (patient.getCheckinterval() > 6 && patient.getCheckinterval() < 12) {
                        patient.setCheckinterval(12.0);
                        toSend.setText(config.ttgThird + "1 год");
                    }
                    else {
                        patient.setCheckinterval(6.0);
                        toSend.setText(config.ttgThird + "6 месяцев");
                    }
                }
            } else if (patient.getTreatment() > 0.0) {
                if (ttg < patient.getLowthslev()) {
                    if (patient.getPathologyName().contains("фолликулярная карцинома")) {
                        sb.append(config.hasTreatmentTtgFirst);
                        toSend.setText(sb.toString());
                        toSend.setReplyMarkup(keyboards.getPatientLolTtgKeyboard());
                        notifyPatient(patient);
                        return toSend;
                    } else {
                        double mkg = (patient.getWeight() < 55 || patient.getAge() > 70) ? 12.5 : 25.0;
                        sb.append("Вам, вероятно, нужно снизить дозу на ");
                        sb.append(mkg);
                        sb.append(" мкг. Затем следует проконтролировать ТТГ через 2 месяца.");
                        patient.setCheckinterval(2.0);
                        toSend.setText(sb.toString());
                    }

                } else if (ttg > patient.getUpthslev()) {
                        double mkg = (patient.getWeight() < 55 || patient.getAge() > 70) ? 12.5 : 25.0;
                        sb.append("Вам, вероятно, нужно увеличить дозу на ");
                        sb.append(mkg);
                        sb.append(" мкг. Затем следует проконтролировать ТТГ через 2 месяца.");
                        patient.setCheckinterval(2.0);
                        toSend.setText(sb.toString());
                } else if (ttg > 10){
                    sb.append("У вас выраженный недостаток гормонов. Вам нужно срочно увеличить дозу на 25 мкг. Контроль ТТГ через 1,5 месяца.");
                    patient.setCheckinterval(1.5);
                    toSend.setText(sb.toString());
                } else {
                    sb.append("У вас отличный уровень ТТГ.  Продолжайте принимать ");
                    sb.append(patient.getTreatmentDrug());
                    sb.append(" по ");
                    sb.append(patient.getTreatment());
                    sb.append(" мкг. Следующий раз контроль через 6 месяцев.");
                    toSend.setText(sb.toString());
                }
            } else {
                sb.append("У вас отличный уровень ТТГ.  Продолжайте принимать ");
                sb.append(patient.getTreatmentDrug());
                sb.append(" по ");
                sb.append(patient.getTreatment());
                sb.append(" мкг. Следующий раз контроль через 6 месяцев.");
                toSend.setText(sb.toString());
            }

            registrationCache.deletePatientById(patientId);
            repository.save(patient);
            stateCache.setState(patientId, PatientState.AWAY);

            toSend.setReplyMarkup(controlKeyboards.controlButtons);
            
            notifyPatient(patient);
            

            return toSend;
        } catch (NumberFormatException e) {
            return error(patientId);
        }
    }


    private void notifyPatient(Patient patient) {
        notificationRepository.deleteByPatientId(patient.getId());

        Set<Notification> notifications = new HashSet<>();
        for (int i = 0; i < 5; ++i) {
            long date = patient.getThsDate() + MONTH_IN_SECONDS * Math.round(patient.getCheckinterval()) + WEEK_IN_SECONDS * i;
            if (Instant.now().getEpochSecond() < date) {
                Notification notification = Notification.builder()
                    .patientId(patient.getId())
                    .text("Пришло время контроля ТТГ!\nЕсли вы сдали анализы, то введите, для этого нажмите кнопку \"Отправить новый результат\"")
                    .date(date)
                    .build();
                notifications.add(notification);
            }
        }

        notificationRepository.saveAll(notifications);
    }

    private SendMessage error(Long patientId) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        toSend.setText("Неверный формат данных. Пришлите число");

        return toSend;
    }

}
