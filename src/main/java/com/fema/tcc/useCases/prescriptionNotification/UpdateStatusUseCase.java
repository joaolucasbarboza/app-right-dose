package com.fema.tcc.useCases.prescriptionNotification;

import com.fema.tcc.domains.enums.Status;
import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.PrescriptionGateway;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import com.fema.tcc.gateways.PrescriptionNotificationHistoryGateway;
import com.fema.tcc.useCases.prescriptionNotificationHistory.SavePrescriptionNotificationHistoryUseCase;
import com.fema.tcc.useCases.user.UserUseCase;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateStatusUseCase {

  private final DeleteNotificationUseCase deleteNotificationUseCase;
  private final SavePrescriptionNotificationHistoryUseCase
      savePrescriptionNotificationHistoryUseCase;
  private final PrescriptionNotificationGateway notificationGateway;
  private final PrescriptionGateway prescriptionGateway;
  private final PrescriptionNotificationHistoryGateway prescriptionNotificationHistoryGateway;
  private final GenerateNotificationsFlow generateNotificationsFlow;
  private final UserUseCase userUseCase;

  public PrescriptionNotification execute(
      Long notificationId, PrescriptionNotification updateRequest) {

    Integer currentUserId = userUseCase.getCurrentUser();

    PrescriptionNotification notification = notificationGateway.findById(notificationId);

    if (!notification.getPrescription().getUser().getId().equals(currentUserId)) {
      throw new IllegalArgumentException("Usuário não autorizado para alterar essa notificação.");
    }

    if (notification.getStatus().equals(updateRequest.getStatus())) {
      log.info("O status da notificação já está definido como {}", updateRequest.getStatus());
      throw new IllegalArgumentException(
          "O status da notificação já está definido como " + updateRequest.getStatus() + ".");
    }

    if (notification.getStatus() != Status.CONFIRMED
        && updateRequest.getStatus().equals(Status.CONFIRMED)) {

      notification.setStatus(updateRequest.getStatus());
      notification.setUpdatedAt(LocalDateTime.now());

      savePrescriptionNotificationHistoryUseCase.execute(notification);

      if (notification.getPrescription().isIndefinite()) {
        generateNotificationsFlow.execute(notification.getPrescription(), 1);
      }

      deleteNotificationUseCase.execute(notificationId);

      Prescription prescription = notification.getPrescription();

      Long countStatusPending = notificationGateway.countPendingById(prescription.getId());
      Long countStatusConfirmed =
          prescriptionNotificationHistoryGateway.countStatusConfirmedByPrescriptionId(
              prescription.getId());

      prescription.setTotalConfirmed(countStatusConfirmed);
      prescription.setTotalPending(countStatusPending);

      prescriptionGateway.save(prescription);
    }

    return notification;
  }
}
