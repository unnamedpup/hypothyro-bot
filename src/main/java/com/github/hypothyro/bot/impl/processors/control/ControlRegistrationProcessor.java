package com.github.hypothyro.bot.impl.processors.control;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.keyboards.registration.RegistrationKeyboards;
import com.github.hypothyro.bot.processors.ControlProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.PatientRepository;
import com.github.hypothyro.service.impl.DefaultDateChecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("AWAY")
@Slf4j
public class ControlRegistrationProcessor implements ControlProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private PatientRepository repository;
    @Autowired private RegistrationCache cache;
    @Autowired private RegistrationKeyboards registrationKeybords;


    public SendMessage processRegistrationField(Message msg) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(msg.getChatId().toString());
        cache.deletePatientById(msg.getChatId());

        Optional<Patient> patient = Optional.empty();

        switch(msg.getText()) {
            case("Отправить новый результат"):
                log.info("Отправить новый результат");
                toSend.setText("Дата нового анализа");
                stateCache.setState(msg.getChatId(), PatientState.AWAY_REGISTER_DATE);
                break;
            case("Когда нужно сдать следующий анализ ?"):
                log.info("Когда нужно сдать следующий анализ ?");
                patient = repository.findById(msg.getChatId());
                if (patient.isPresent()) {
                    log.info("Patient: {}", patient.get());
                    LocalDate nextDate = calculateNextAnalysDate(patient.get());
                    String res = nextDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    log.info("Next date: {}", res);
                    toSend.setText("Дата сдачи следующего анализа " + res);
                } else {
                    toSend.setText("Вас нет в базе");
                }
                break;
            case("Последние мои результаты"):
                log.info("Последние мои результаты");
                patient = repository.findById(msg.getChatId());

                if (patient.isPresent()) {
                    LocalDate date = new DefaultDateChecker(patient.get().getThsDate()).getDateAsLocalDate();
                    double result = patient.get().getThsResult();

                    toSend.setText("Результат анализа ТТГ:\n\nДата: " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\nЗначение: " + result);
                } else {
                    toSend.setText("Вас нет в базе");
                }
                break;

            case("Новая терапия"):
                log.info("New treatment");
                toSend.setText("Выберите новую терапию");
                toSend.setReplyMarkup(registrationKeybords.getDrugRegistrationKeyboard());
                stateCache.setState(msg.getChatId(), PatientState.AWAY_CHANGE_TREATMENT_NAME);
                break;
            default:
                log.info("Ignoring unknown text: {}", msg.getText());
                return null;
        }


        log.info("Control registration processor");

        return toSend;
    }

    private LocalDate calculateNextAnalysDate(Patient patient) {
        LocalDate lastDate = Instant
            .ofEpochSecond(patient.getThsDate() == null ? patient.getDateOperation() : patient.getThsDate())
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

        String[] monthsAndDays = patient.getCheckinterval().toString().split("\\.");
        log.info(patient.getCheckinterval().toString());
        long months = Long.parseLong(monthsAndDays[0]);
        long days = Long.parseLong(monthsAndDays[1]);

        log.info("Monts: {}, days: {}", monthsAndDays[0], monthsAndDays[1]);
        log.info("Last date: {}", lastDate);

        LocalDate newDate = lastDate
            .plusMonths(months)
            .plusDays(days > 0 ? 30 / days : 0);

        return newDate;
    }

}


