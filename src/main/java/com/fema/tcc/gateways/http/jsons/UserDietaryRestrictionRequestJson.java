package com.fema.tcc.gateways.http.jsons;

import jakarta.validation.constraints.NotNull;

public record UserDietaryRestrictionRequestJson(
    @NotNull String notes, @NotNull Integer dietaryRestrictionId) {}
