package com.fema.tcc.usecases.prescriptionNotification;

import static com.fema.tcc.utils.NotificationIntervalUtil.calculateInterval;

import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NextNotificationUseCase {

  final PrescriptionNotificationGateway prescriptionNotificationGateway;

  public List<LocalDateTime> execute(Prescription prescription, int quantityGenerate) {
    List<LocalDateTime> times = new ArrayList<>();

    List<PrescriptionNotification> notifications =
        prescriptionNotificationGateway.findAllByPrescriptionId(prescription.getId());

    LocalDateTime current;

    if (notifications == null || notifications.isEmpty()) {
      current = LocalDateTime.now();
    } else {
      current = notifications.getLast().getNotificationTime();
    }

    Duration interval = calculateInterval(prescription);

    for (int i = 0; i < quantityGenerate; i++) {
      current = current.plus(interval);
      times.add(current);
    }

    return times;
  }
}
