package com.fema.tcc.useCases.prescriptionNotificationHistory;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.domains.prescriptionNotificationHistory.PrescriptionNotificationHistory;
import com.fema.tcc.gateways.PrescriptionNotificationHistoryGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SavePrescriptionNotificationHistoryUseCase {

  private PrescriptionNotificationHistoryGateway gateway;

  public void execute(PrescriptionNotification notification) {
    PrescriptionNotificationHistory history =
        PrescriptionNotificationHistory.builder()
            .prescription(notification.getPrescription())
            .notificationId(notification.getId())
            .status(notification.getStatus())
            .confirmedAt(notification.getUpdatedAt())
            // todo: criar na notification o confirmedAt e n usar o updatedAt
            .createdAt(notification.getUpdatedAt())
            .build();

    gateway.save(history);
  }
}
