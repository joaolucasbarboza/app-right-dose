package com.fema.tcc.usecases.prescriptionNotification.steps;

import com.fema.tcc.domains.enums.Status;
import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BuildNotifications {

  public List<PrescriptionNotification> execute(
      Prescription prescription, List<LocalDateTime> times) {
    List<PrescriptionNotification> notifications = new ArrayList<>();

    for (LocalDateTime time : times) {
      PrescriptionNotification notification =
          PrescriptionNotification.builder()
              .prescription(prescription)
              .notificationTime(time)
              .status(Status.PENDING)
              .createdAt(LocalDateTime.now())
              .build();

      notifications.add(notification);
    }

    return notifications;
  }
}
