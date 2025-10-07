package com.fema.tcc.usecases.prescriptionNotification;

import com.fema.tcc.domains.enums.Frequency;
import com.fema.tcc.domains.prescription.Prescription;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class CalculateEndDate {

  public LocalDateTime execute(Prescription prescription) {
    if (prescription.isIndefinite()) {
      return null;
    }

    Frequency freq = prescription.getUomFrequency();
    int value = prescription.getFrequency();
    int totalOccurrences = prescription.getTotalOccurrences();

    LocalDateTime start = prescription.getStartDate();

    if (freq == Frequency.DAILY) {
      return start.plusDays((long) value * (totalOccurrences - 1));
    } else if (freq == Frequency.HOURLY) {
      return start.plusHours((long) value * (totalOccurrences - 1));
    } else {
      throw new IllegalArgumentException("Unsupported frequency: " + freq);
    }
  }
}
