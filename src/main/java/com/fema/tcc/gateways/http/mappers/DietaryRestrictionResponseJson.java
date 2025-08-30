package com.fema.tcc.gateways.http.mappers;

public record DietaryRestrictionResponseJson(
        Long id,
        String code,
        String description
) {
}
