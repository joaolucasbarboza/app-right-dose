package com.fema.tcc.useCases.recommendationAi;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OllamaClient {
  private final WebClient webClient;

  public OllamaClient(@Value("${spring.ai.ollama.base-url}") String baseUrl) {
    this.webClient = WebClient.builder().baseUrl(baseUrl).build();
  }

  public Mono<String> chat(Map<String, Object> payload) {
    return webClient
        .post()
        .uri("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(payload)
        .exchangeToMono(
            resp -> {
              if (resp.statusCode().is2xxSuccessful()) {
                return resp.bodyToMono(String.class);
              }
              return resp.bodyToMono(String.class)
                  .defaultIfEmpty("")
                  .flatMap(
                      body ->
                          Mono.error(
                              new IllegalStateException(
                                  "Ollama " + resp.statusCode().value() + ": " + body)));
            });
  }
}
