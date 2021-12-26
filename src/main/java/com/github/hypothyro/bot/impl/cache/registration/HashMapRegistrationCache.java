package com.github.hypothyro.bot.impl.cache.registration;

import java.util.HashMap;
import java.util.Map;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HashMapRegistrationCache implements RegistrationCache {

    private final Map<Long, Patient> cache = new HashMap<>();

    @Autowired private PatientRepository repository;

    @Override
    public Patient getPatientById(Long userId) {
        Patient patientFromCache = cache.get(userId);

        if (patientFromCache == null) {
            // Patient patientFromDb = repository.getById(userId);
            Patient patientFromDb = null;
            if (patientFromDb == null) {
                patientFromCache = Patient.builder().id(userId).build();
                setPatient(userId, patientFromCache);
            } else {
                savePatient(patientFromDb);
                return patientFromDb;
            }

        }

        return patientFromCache;
    }

    @Override
    public void setPatient(Long userId, Patient patient) {
        cache.put(userId, patient);
    }

    @Override
    public Map<Long, Patient> getUsersInfo() {
        return cache;
    }

    @Override
    public void deletePatientById(Long userId) {
        cache.remove(userId);
    }

    @Override
    public void savePatient(Patient patient) {
        cache.put(patient.getId(), patient);
    }


}

