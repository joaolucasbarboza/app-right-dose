package com.fema.tcc.gateways.http.jsons;

import com.fema.tcc.domains.enums.UserRole;
import java.util.Date;

public record UserResponseJson(
    Integer id, String name, String email, Date createdAt, UserRole role, String fcmToken) {}
