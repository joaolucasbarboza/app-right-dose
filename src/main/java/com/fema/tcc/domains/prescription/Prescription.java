package com.fema.tcc.domains.prescription;

import com.fema.tcc.domains.enums.DosageUnit;
import com.fema.tcc.domains.enums.Frequency;
import com.fema.tcc.domains.medicine.Medicine;
import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.domains.user.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Prescription {

  private Long id;
  private Medicine medicine;
  private User user;

  private double dosageAmount;
  private DosageUnit dosageUnit;

  private int frequency;
  private Frequency uomFrequency;
  private boolean indefinite;

  private Integer totalOccurrences;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  private boolean wantsNotifications;
  private String instructions;

  private List<PrescriptionNotification> notifications;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public void updateFrom(Prescription source) {
    this.setDosageAmount(source.getDosageAmount());
    this.setDosageUnit(source.getDosageUnit());
    this.setFrequency(source.getFrequency());
    this.setUomFrequency(source.getUomFrequency());
    this.setTotalOccurrences(source.totalOccurrences);
    this.setStartDate(source.getStartDate());
    this.setEndDate(source.getEndDate());
    this.setInstructions(source.getInstructions());
  }
}
