package com.fema.tcc.gateways.http.jsons;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record MedicineResponseJson(
    Integer id,
    String name,
    String description,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC") LocalDateTime createdAt,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC") LocalDateTime updatedAt) {}
