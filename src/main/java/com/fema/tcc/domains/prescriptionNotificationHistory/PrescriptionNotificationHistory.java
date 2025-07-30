package com.fema.tcc.domains.prescriptionNotificationHistory;

import com.fema.tcc.domains.enums.Status;
import com.fema.tcc.domains.prescription.Prescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionNotificationHistory {

    private Long id;
    private Long notificationId;
    private Prescription prescription;
    private Status status;
    private LocalDateTime confirmedAt;
    private LocalDateTime createdAt;
}
