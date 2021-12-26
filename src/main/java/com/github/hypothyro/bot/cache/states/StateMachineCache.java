package com.github.hypothyro.bot.cache.states;

import java.util.Map;

import com.github.hypothyro.domain.PatientState;

public interface StateMachineCache {
    PatientState getStateById(Long userId);
    void setState(Long userId, PatientState state);
    Map<Long, PatientState> getUsersInfo();
}
