package com.github.hypothyro.service.impl;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.hypothyro.domain.Notification;
import com.github.hypothyro.repository.NotificationRepository;
import com.github.hypothyro.service.NotificationService;
import com.github.hypothyro.service.NotificationStateCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultNotificationService implements NotificationService {

    @Autowired private NotificationRepository repository;
    @Autowired private Map<String, NotificationStateCreator> creators;

    private final ScheduledExecutorService compareWorkersPool = Executors.newSingleThreadScheduledExecutor();

    public void updateNotifications() {
        compareWorkersPool.scheduleAtFixedRate(() -> {
                doUpdate();
        }, 0, 30, TimeUnit.MINUTES);
    }

    private void doUpdate() {
        Pageable pageRequest = PageRequest.of(0, 200);
        Page<Notification> onePage = repository.findAll(pageRequest);

        while (!onePage.isEmpty()) {
            pageRequest = pageRequest.next();

            for (Notification n : onePage) {
                // log.info("Notification date {}, current date {}", n, Instant.now().getEpochSecond());
                processNotification(n);
            }

            onePage = repository.findAll(pageRequest);
        }
    }


    @SneakyThrows
    private void processNotification(Notification n) {
        if (readyToSend(n)) {
            creators.get(n.getState().toString()).createState(n);
            repository.delete(n);
        }
    }

    private boolean readyToSend(Notification n) {
        return n.getDate() < Instant.now().getEpochSecond();
    }
}
