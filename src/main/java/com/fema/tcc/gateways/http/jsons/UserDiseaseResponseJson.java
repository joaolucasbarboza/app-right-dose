package com.fema.tcc.gateways.http.jsons;

import com.fema.tcc.domains.disease.Disease;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record UserDiseaseResponseJson(
    Integer id,
    Disease disease,
    String notes,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAt) {}
