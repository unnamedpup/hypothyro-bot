package com.github.hypothyro.service.impl;

import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.repository.PatientRepository;
import com.github.hypothyro.service.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultRegistrationService implements RegistrationService {

    @Autowired
    private PatientRepository repository;

    public Patient register(Patient patient) {
        log.info("Patient for registering: {}", patient);
        return repository.save(patient);
    }
}
