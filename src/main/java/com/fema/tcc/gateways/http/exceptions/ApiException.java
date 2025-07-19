package com.fema.tcc.gateways.http.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Builder
public class ApiException {
  private int statusCode;
  private String message;
  private HttpStatus httpStatus;

  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
  private LocalDateTime timestamp;

  public static ApiException of(int statusCode, String message, HttpStatus httpStatus) {
    return ApiException.builder()
        .statusCode(statusCode)
        .message(message)
        .httpStatus(httpStatus)
        .timestamp(LocalDateTime.now())
        .build();
  }
}
