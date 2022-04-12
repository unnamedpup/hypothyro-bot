/* package com.github.hypothyro.bot.impl.processors.registration.callback;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.bot.processors.RegistrationCallbackQueryProcessor;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.extern.slf4j.Slf4j;

@Service("REGISTRATION_TREATMENT_NAME")
@Slf4j
public class TreatmentDrugCallbackQueryProcessor implements RegistrationCallbackQueryProcessor {

    @Autowired private StateMachineCache stateCache;
    @Autowired private RegistrationCache registrationCache;


    @Override
    public SendMessage processRegistrationCallback(CallbackQuery callback) {
        log.info("RegistrationTreatmentDrugProcessor");
        Message msg = callback.getMessage();
        Long patientId = msg.getChatId();
        Patient patient = registrationCache.getPatientById(patientId);
        String callBackData = callback.getData();
        String treatmentDrug = callBackData.replace("REGISTRATION_PRETREATMENT_NAME_", "").toLowerCase();
        patient.setTreatmentDrug(treatmentDrug);

        SendMessage toSend = new SendMessage();
        toSend.setChatId(patientId.toString());
        if (callBackData.contains("NOTHING")) {
            patient.setTreatment(0.0);
            toSend.setText("LALALALA");
        } else {
            toSend.setText("Введите дозу препарата после операции(в мкг):");
            stateCache.setState(patientId, PatientState.REGISTRATION_TREATMENT_MKG);
        }
        registrationCache.savePatient(patient);

        return toSend;
    }
}
 */
