package com.fema.tcc.gateways.postgresql.entity;

import com.fema.tcc.domains.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "prescription_notification")
@Table(name = "prescription_notification")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PrescriptionNotificationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "prescription_id")
  @NotNull
  private PrescriptionEntity prescription;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "notification_time")
  @NotNull
  private LocalDateTime notificationTime;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
