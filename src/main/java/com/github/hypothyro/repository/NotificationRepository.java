package com.github.hypothyro.repository;

import com.github.hypothyro.domain.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Modifying
    @Transactional
    @Query(value="delete from Notification n where n.patientId = ?1")
    void deleteByPatientId(long patientId);
}
