package com.fema.tcc.gateways.http.jsons;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fema.tcc.domains.enums.DosageUnit;
import com.fema.tcc.domains.enums.Frequency;
import java.time.LocalDateTime;
import java.util.List;

public record PrescriptionWithNotificationsResponseJson(
    Long id,
    MedicineResponseJson medicine,
    double dosageAmount,
    DosageUnit dosageUnit,
    int frequency,
    Frequency uomFrequency,
    int totalOccurrences,
    boolean indefinite,
    Long totalConfirmed,
    Long totalPending,
    LocalDateTime startDate,
    LocalDateTime endDate,
    boolean wantsNotifications,
    List<PrescriptionNotificationResponseJson> notifications,
    String instructions,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC") LocalDateTime createdAt,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC") LocalDateTime updatedAt) {}
