package com.fema.tcc.gateways.postgresql.entity;

import com.fema.tcc.domains.enums.DosageUnit;
import com.fema.tcc.domains.enums.Frequency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "prescription")
@Table(name = "prescription")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PrescriptionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "medicine_id")
  @NotNull
  private MedicineEntity medicine;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @NotNull
  private UserEntity user;

  @Column(name = "dosage_amount")
  @NotNull
  private double dosageAmount;

  @Enumerated(EnumType.STRING)
  @NotNull
  private DosageUnit dosageUnit;

  @NotNull private int frequency;

  @Enumerated(EnumType.STRING)
  @Column(name = "uom_frequency")
  @NotNull
  private Frequency uomFrequency;

  @Column(name = "total_Days")
  @NotNull
  private int totalDays;

  @Column(name = "start_date")
  @NotNull
  private LocalDateTime startDate;

  @Column(name = "end_date")
  @NotNull
  private LocalDateTime endDate;

  @Column(name = "wants_notifications")
  @NotNull
  private boolean wantsNotifications;

  private String instructions;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
