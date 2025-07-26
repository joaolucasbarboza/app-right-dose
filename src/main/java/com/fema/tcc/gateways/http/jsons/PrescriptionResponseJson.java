package com.fema.tcc.gateways.http.jsons;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fema.tcc.domains.enums.DosageUnit;
import com.fema.tcc.domains.enums.Frequency;
import java.time.LocalDateTime;

public record PrescriptionResponseJson(
    Long id,
    MedicineResponseJson medicine,
    double dosageAmount,
    DosageUnit dosageUnit,
    int frequency,
    Frequency uomFrequency,
    Integer totalOccurrences,
    LocalDateTime startDate,
    LocalDateTime endDate,
    boolean wantsNotifications,
    String instructions,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC") LocalDateTime createdAt,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC") LocalDateTime updatedAt) {}
