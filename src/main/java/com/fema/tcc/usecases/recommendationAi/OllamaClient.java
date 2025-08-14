package com.fema.tcc.usecases.recommendationAi;

import com.fema.tcc.gateways.http.jsons.OllamaChatResponse;
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

  public Mono<OllamaChatResponse> chat(Object req) {
    return webClient
        .post()
        .uri("/api/chat")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(req)
        .retrieve()
        .bodyToMono(OllamaChatResponse.class);
  }
}
