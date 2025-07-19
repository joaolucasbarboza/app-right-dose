package com.fema.tcc.gateways.http.jsons;

public record NotificationPayloadJson(Long notificationId, String userToken, String message) {}
