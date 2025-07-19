package com.fema.tcc.gateways.impl;

import com.fema.tcc.gateways.NotificationGateway;
import com.fema.tcc.gateways.StringConverterGateway;
import com.fema.tcc.gateways.http.jsons.NotificationPayloadJson;
import com.fema.tcc.gateways.rabbitmq.NotificationProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationGatewayImpl implements NotificationGateway {

  private final StringConverterGateway<NotificationPayloadJson> converter;
  private final NotificationProducer producer;

  @Override
  public void publish(NotificationPayloadJson payloadJson) {
    try {
      String message = converter.toString(payloadJson);
      producer.send(message);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
