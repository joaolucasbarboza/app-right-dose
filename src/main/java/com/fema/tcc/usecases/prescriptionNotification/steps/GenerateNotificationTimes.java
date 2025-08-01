package com.fema.tcc.usecases.prescriptionNotification.steps;

import com.fema.tcc.domains.prescription.Prescription;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import static com.fema.tcc.utils.NotificationIntervalUtil.calculateInterval;

@Component
public class GenerateNotificationTimes {

  public List<LocalDateTime> execute(Prescription prescription, LocalDateTime end) {
    List<LocalDateTime> times = new ArrayList<>();

    LocalDateTime current = prescription.getStartDate();
    Duration interval = calculateInterval(prescription);

    while (!current.isAfter(end)) {
      times.add(current);
      current = current.plus(interval);
    }

    return times;
  }
}
