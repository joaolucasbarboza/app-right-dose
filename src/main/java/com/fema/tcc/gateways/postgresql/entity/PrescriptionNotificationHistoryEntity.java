package com.fema.tcc.gateways.postgresql.entity;

import com.fema.tcc.domains.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "prescription_notification_history")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionNotificationHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    @NotNull
    private PrescriptionEntity prescription;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime confirmedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
}
