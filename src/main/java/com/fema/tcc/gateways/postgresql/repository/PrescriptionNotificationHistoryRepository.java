package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.PrescriptionNotificationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionNotificationHistoryRepository extends JpaRepository<PrescriptionNotificationHistoryEntity, Long> {
}
