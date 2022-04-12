package com.github.hypothyro.bot.impl.processors.control;

import java.time.format.DateTimeParseException;
import java.util.Optional;

import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.processors.ControlProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;
import com.github.hypothyro.repository.PatientRepository;
import com.github.hypothyro.service.impl.DefaultDateChecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service("AWAY_REGISTER_DATE")
public class NewAnalysDate implements ControlProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private PatientRepository repository;

    public SendMessage processRegistrationField(Message msg) {
        SendMessage toSend = new SendMessage();
        toSend.setChatId(msg.getChatId().toString());
        DefaultDateChecker dateChecker;

        try {
            dateChecker = new DefaultDateChecker(msg.getText());
        } catch(DateTimeParseException e) {
            toSend.setText("Неправильный формат данных.(введите в формате дд.мм.гггг)");
            return toSend;
        } 

        if (dateChecker.isEarlierThen2Months()) {
            Optional<Patient> patient_opt = repository.findById(msg.getChatId());
            if (patient_opt.isEmpty()) {
                toSend.setText("Вас нет в базе");
                stateCache.setState(msg.getChatId(), PatientState.AWAY);
                return toSend;
            }
            Patient patient = patient_opt.get();
            patient.setThsDate(dateChecker.getDate());
            repository.save(patient);
            stateCache.setState(msg.getChatId(), PatientState.REGISTRATION_TTG_RESULT);
            toSend.setText("Введите результат ТТГ");
        } else {
            toSend.setText("Слишком старый анализ(срок действия анализа 2 месяца).");
            stateCache.setState(msg.getChatId(), PatientState.AWAY);
        }


        return toSend;
    }
}



