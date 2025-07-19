package com.fema.tcc.gateways;

public interface StringConverterGateway<T> {
  String toString(T object);

  T fromString(String string);
}
