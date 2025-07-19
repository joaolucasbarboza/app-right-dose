package com.fema.tcc.gateways.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

  @Value("${queue.name}")
  private String queueName;

  private final RabbitTemplate rabbitTemplate;

  public NotificationProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void send(String message) {
    rabbitTemplate.convertAndSend(queueName, message);
  }
}
