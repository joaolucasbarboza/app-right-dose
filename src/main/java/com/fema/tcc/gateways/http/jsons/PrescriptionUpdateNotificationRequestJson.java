package com.fema.tcc.gateways.http.jsons;

import com.fema.tcc.domains.enums.Status;

public record PrescriptionUpdateNotificationRequestJson(Long notificationId, Status status) {}
