package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.PrescriptionNotificationEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Query(
      "SELECT n FROM prescription_notification n "
          + "JOIN n.prescription p "
          + "WHERE p.id = :prescriptionId "
          + "ORDER BY n.notificationTime ASC")
  List<PrescriptionNotificationEntity> findAllByPrescriptionId(
      @Param("prescriptionId") Long prescriptionId);

  @Query(
"""
  SELECT n
  FROM prescription_notification n
  JOIN n.prescription p
  JOIN p.user u
  WHERE u.id = :userId
    AND n.status = 'PENDING'
    AND n.notificationTime > :now
""")
  Page<PrescriptionNotificationEntity> findAllByUserId(
      @Param("userId") Integer userId, @Param("now") LocalDateTime now, Pageable pageable);

  @Query(
      "SELECT COUNT(n) FROM prescription_notification n "
          + "WHERE n.prescription.id = :prescriptionId AND n.status = 'PENDING'")
  Long countPending(@Param("prescriptionId") Long prescriptionId);
}
