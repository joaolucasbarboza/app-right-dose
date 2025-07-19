package com.fema.tcc.gateways;

import com.fema.tcc.gateways.http.jsons.NotificationPayloadJson;

public interface NotificationGateway {
  void publish(NotificationPayloadJson payloadJson);
}
