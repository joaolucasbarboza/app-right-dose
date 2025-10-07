package com.fema.tcc.usecases.notification;

import com.fema.tcc.domains.enums.Status;
import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateStatusNotificationUseCase {

  private final PrescriptionNotificationGateway prescriptionNotificationGateway;

  public void execute(Long notificationId) {
    PrescriptionNotification prescriptionNotification =
        prescriptionNotificationGateway.findById(notificationId);
    prescriptionNotification.setStatus(Status.SENT);
    prescriptionNotification.setUpdatedAt(java.time.LocalDateTime.now());
    prescriptionNotificationGateway.update(prescriptionNotification);
  }
}
