package com.fema.tcc.useCases.prescriptionNotification;

import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.PrescriptionGateway;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import com.fema.tcc.useCases.prescriptionNotification.steps.BuildNotifications;
import com.fema.tcc.useCases.prescriptionNotification.steps.GenerateNotificationTimes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GenerateNotificationsFlow {

  private final NextNotificationUseCase nextNotificationUseCase;
  private final GenerateNotificationTimes generateNotificationTimes;
  private final BuildNotifications buildNotifications;
  private final PrescriptionNotificationGateway gateway;
  private final PrescriptionGateway prescriptionGateway;

  public void execute(Prescription prescription, int quantityGenerate) {

    List<LocalDateTime> times = new ArrayList<>();

    if (prescription.isIndefinite()) {
      List<LocalDateTime> nextNotifications =
          nextNotificationUseCase.execute(prescription, quantityGenerate);
      times.addAll(nextNotifications);
    } else {
      List<LocalDateTime> notificationTimes =
          generateNotificationTimes.execute(prescription, prescription.getEndDate());
      times.addAll(notificationTimes);
    }

    List<PrescriptionNotification> notifications = buildNotifications.execute(prescription, times);

    gateway.saveAll(notifications);

    prescription.setTotalPending((long) times.size());
    prescriptionGateway.save(prescription);
  }
}
