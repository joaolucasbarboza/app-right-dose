package com.fema.tcc.gateways.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fema.tcc.gateways.StringConverterGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JsonStringConverter<T> implements StringConverterGateway<T> {

  private final ObjectMapper objectMapper;
  private final Class<T> type;

  @Override
  public String toString(T object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException("Error converting object to JSON string", e);
    }
  }

  @Override
  public T fromString(String string) {
    try {
      return objectMapper.readValue(string, type);
    } catch (Exception e) {
      throw new RuntimeException("Error converting JSON string to object", e);
    }
  }
}
