package com.fema.tcc.gateways.http.jsons;

import com.fema.tcc.domains.enums.DosageUnit;
import com.fema.tcc.domains.enums.Frequency;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BuildDetailsPrescriptions(
    double dosageAmount,
    String name,
    String description,
    DosageUnit dosageUnit,
    int frequency,
    Frequency uomFrequency,
    boolean indefinite,
    int totalOccurrences,
    LocalDateTime startDate,
    LocalDateTime endDate,
    String instructions) {}
