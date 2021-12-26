package com.github.hypothyro.service.impl;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.hypothyro.bot.HypothyroBot;
import com.github.hypothyro.domain.Notification;
import com.github.hypothyro.repository.NotificationRepository;
import com.github.hypothyro.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultNotificationService implements NotificationService {

    @Autowired private HypothyroBot bot;
    @Autowired private NotificationRepository repository;

    private final ScheduledExecutorService compareWorkersPool = Executors.newSingleThreadScheduledExecutor();

    public void updateNotifications() {
        compareWorkersPool.scheduleAtFixedRate(() -> {
                doUpdate();
        // TODO: Change to minutes or hours
        }, 0, 30, TimeUnit.MILLISECONDS);
    }

    @SneakyThrows
    private void doUpdate() {
        Pageable pageRequest = PageRequest.of(0, 200);
        Page<Notification> onePage = repository.findAll(pageRequest);

        while (!onePage.isEmpty()) {
            pageRequest = pageRequest.next();

            for (Notification n : onePage) {
                // TODO: Remove test logging
                log.info("Notification date {}, current date {}", n, Instant.now().getEpochSecond());
                if (n.getDate() < Instant.now().getEpochSecond()) {
                    SendMessage msg = SendMessage.builder()
                        .chatId(n.getPatientId().toString())
                        .text(n.getText())
                        .build();
                    bot.execute(msg);
                    repository.delete(n);
                }
            }

            onePage = repository.findAll(pageRequest);
        }
    }
}
