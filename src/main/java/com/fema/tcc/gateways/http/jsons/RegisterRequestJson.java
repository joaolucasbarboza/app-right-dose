package com.fema.tcc.gateways.http.jsons;

import com.fema.tcc.domains.enums.UserRole;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestJson(
    @NotNull String name,
    @NotNull String email,
    @NotNull String password,
    @NotNull UserRole role) {}
