package com.fema.tcc.gateways.http.jsons;

import com.fema.tcc.domains.enums.Status;

import java.time.LocalDateTime;

public record PrescriptionNotificationResponseJson(
    Long id,
    LocalDateTime notificationTime,
    Status status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {
}
