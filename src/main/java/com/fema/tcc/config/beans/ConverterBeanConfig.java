package com.fema.tcc.config.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fema.tcc.gateways.StringConverterGateway;
import com.fema.tcc.gateways.http.jsons.NotificationPayloadJson;
import com.fema.tcc.gateways.impl.JsonStringConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterBeanConfig {

  @Bean
  public StringConverterGateway<NotificationPayloadJson>
      notificationPayloadJsonStringConverterGateway(ObjectMapper objectMapper) {
    return new JsonStringConverter<>(objectMapper, NotificationPayloadJson.class);
  }
}
