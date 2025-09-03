package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.PrescriptionNotificationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrescriptionNotificationHistoryRepository
    extends JpaRepository<PrescriptionNotificationHistoryEntity, Long> {

  @Query(
      "SELECT COUNT(n) FROM prescription_notification_history n "
          + "WHERE n.prescription.id = :prescriptionId AND n.status = 'CONFIRMED'")
  Long countConfirmed(@Param("prescriptionId") Long prescriptionId);
}
