package com.fema.tcc.gateways;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrescriptionNotificationGateway {

  void saveAll(List<PrescriptionNotification> prescriptions);

  PrescriptionNotification save(PrescriptionNotification prescriptionNotification);

  void update(PrescriptionNotification prescriptionNotification);

  PrescriptionNotification findById(Long id);

  Page<PrescriptionNotification> findAllByUserId(Integer userId, Pageable pageable);

  List<PrescriptionNotification> findAllReadyToNotify(LocalDateTime now, LocalDateTime limitTime);

  List<PrescriptionNotification> findAllByPrescriptionId(Long prescriptionId);

  void deleteById(Long id);
}
