package com.fema.tcc.gateways.rabbitmq;

import com.fema.tcc.usecases.notification.SendNotificationFirebaseUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class NotificationConsumer {

  private final SendNotificationFirebaseUseCase sendNotificationFirebaseUseCase;

  @RabbitListener(queues = "${queue.name}")
  public void consume(String message) {
    try {
      log.info(message);
      sendNotificationFirebaseUseCase.execute(message);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
