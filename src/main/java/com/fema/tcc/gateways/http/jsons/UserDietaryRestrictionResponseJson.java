package com.fema.tcc.gateways.http.jsons;

import com.fema.tcc.domains.dietaryRestriction.DietaryRestriction;

public record UserDietaryRestrictionResponseJson(
    Integer id,
    Integer userId,
    DietaryRestriction dietaryRestriction,
    String notes,
    String createdAt,
    String updatedAt) {}
