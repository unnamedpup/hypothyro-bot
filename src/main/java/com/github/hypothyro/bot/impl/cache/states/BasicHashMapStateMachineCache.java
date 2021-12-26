package com.github.hypothyro.bot.impl.cache.states;

import java.util.HashMap;
import java.util.Map;

import com.github.hypothyro.bot.cache.states.StateMachineCache;
import com.github.hypothyro.domain.PatientState;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BasicHashMapStateMachineCache implements StateMachineCache {

    private final Map<Long, PatientState> cache = new HashMap<>();

    @Override
    public PatientState getStateById(Long userId) {
        log.info("Getting state for user {}", userId);

        PatientState currentState = cache.get(userId);

        if (currentState == null) {
            setState(userId, PatientState.BASIC);
            return PatientState.BASIC;
        }

        return currentState;
    }

    @Override
    public void setState(Long userId, PatientState state) {
        log.info("Setting state {} for user {}", state, userId);
        cache.put(userId, state);
    }

    @Override
    public Map<Long, PatientState> getUsersInfo() {
        return cache;
    }
}
