package com.github.hypothyro.bot.cache.registration;

import java.util.Map;

import com.github.hypothyro.domain.Patient;

public interface RegistrationCache {
    Patient getPatientById(Long userId);
    @Deprecated
    void setPatient(Long userId, Patient patient);
    void savePatient(Patient patient);
    Map<Long, Patient> getUsersInfo();
    void deletePatientById(Long userId);
}

