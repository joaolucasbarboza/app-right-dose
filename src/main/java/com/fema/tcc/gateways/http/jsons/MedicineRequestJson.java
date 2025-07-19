package com.fema.tcc.gateways.http.jsons;

import jakarta.validation.constraints.NotNull;

public record MedicineRequestJson(
    @NotNull(message = "medicine.name.blank") String name,
    String description) {}
