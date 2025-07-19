package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.PrescriptionNotificationEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrescriptionNotificationRepository
    extends JpaRepository<PrescriptionNotificationEntity, Long> {

  @Query(
      "SELECT n FROM prescription_notification n "
          + "WHERE n.notificationTime between :now AND :limitTime "
          + "AND n.status = 'PENDING'")
  List<PrescriptionNotificationEntity> findAllReadyToNotify(
      @Param("now") LocalDateTime now, @Param("limitTime") LocalDateTime limitTime);
}
