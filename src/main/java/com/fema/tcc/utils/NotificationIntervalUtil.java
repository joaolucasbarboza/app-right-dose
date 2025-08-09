package com.fema.tcc.utils;

import com.fema.tcc.domains.enums.Frequency;
import com.fema.tcc.domains.prescription.Prescription;
import java.time.Duration;

public final class NotificationIntervalUtil {

  private NotificationIntervalUtil() {}

  public static Duration calculateInterval(Prescription prescription) {
    Frequency freq = prescription.getUomFrequency();
    int value = prescription.getFrequency();

    if (freq.equals(Frequency.HOURLY)) {
      return Duration.ofHours(value);
    } else if (freq.equals(Frequency.DAILY)) {
      return Duration.ofDays(value);
    } else {
      throw new IllegalArgumentException("Unsupported frequency: " + freq);
    }
  }
}
