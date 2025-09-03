package com.fema.tcc.gateways.http.jsons;

import jakarta.validation.constraints.NotNull;

public record UserDiseaseRequestJson(@NotNull Integer diseaseId, @NotNull String notes) {}
