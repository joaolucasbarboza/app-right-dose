package com.fema.tcc.gateways;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import java.time.LocalDateTime;
import java.util.List;

public interface PrescriptionNotificationGateway {

  void saveAll(List<PrescriptionNotification> prescriptions);

  PrescriptionNotification save(PrescriptionNotification prescriptionNotification);

  void update(PrescriptionNotification prescriptionNotification);

  PrescriptionNotification findById(Long id);

  List<PrescriptionNotification> findAllReadyToNotify(LocalDateTime now, LocalDateTime limitTime);

  List<PrescriptionNotification> findAllByPrescriptionId(Long prescriptionId);
}
