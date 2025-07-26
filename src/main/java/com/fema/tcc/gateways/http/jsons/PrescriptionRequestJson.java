package com.fema.tcc.gateways.http.jsons;

import com.fema.tcc.domains.enums.DosageUnit;
import com.fema.tcc.domains.enums.Frequency;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PrescriptionRequestJson(
    @NotNull Integer medicineId,
    @NotNull double dosageAmount,
    @NotNull DosageUnit dosageUnit,
    @NotNull int frequency,
    @NotNull Frequency uomFrequency,
    @NotNull boolean indefinite,
    Integer totalOccurrences,
    @NotNull LocalDateTime startDate,
    @NotNull boolean wantsNotifications,
    @NotNull String instructions) {}
