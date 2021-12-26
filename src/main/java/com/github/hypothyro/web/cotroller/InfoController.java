package com.github.hypothyro.web.cotroller;

import java.util.Map;

import com.github.hypothyro.bot.cache.registration.RegistrationCache;
import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.domain.PatientState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/v1/info")
@RestController
public class InfoController {

    @Autowired
    private StateMachineCache stateCache;

    @Autowired
    private  RegistrationCache registrationCache;

    @GetMapping("/states")
    public ResponseEntity<Map<Long, PatientState>> getStates() {
        return new ResponseEntity<>(stateCache.getUsersInfo(), HttpStatus.OK);
    }

    @GetMapping("/registration")
    public ResponseEntity<Map<Long, Patient>> getRegistration() {
        return new ResponseEntity<>(registrationCache.getUsersInfo(), HttpStatus.OK);
    }

}
