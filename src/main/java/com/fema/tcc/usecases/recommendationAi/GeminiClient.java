package com.fema.tcc.usecases.recommendationAi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GeminiClient {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final WebClient webClient;
    private final String apiKey;
    private final String defaultModel;

    public GeminiClient(
            @Value("${gemini.api.base-url}")
            String baseUrl,

            @Value("${gemini.api.key}")
            String apiKey,

            @Value("${gemini.api.model}")
            String defaultModel) {

        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.defaultModel = defaultModel;
    }

    public Mono<String> chat(Map<String, Object> payload) {
        try {
            String model = String.valueOf(payload.getOrDefault("targetModel", defaultModel));
            String prompt = String.valueOf(payload.getOrDefault("prompt", ""));
            double temperature = 0.2;
            Object options = payload.get("options");
            if (options instanceof Map<?, ?> opt && opt.get("temperature") != null) {
                try { temperature = Double.parseDouble(String.valueOf(opt.get("temperature"))); } catch (Exception ignored) {}
            }

            String pathModel = model.startsWith("models/") ? model : "models/" + model;

            var parts = splitSystemAndUser(prompt);
            String systemText = parts.system();
            String userText = parts.user();

            Map<String, Object> generationConfig = Map.of(
                    "temperature", temperature,
                    "response_mime_type", "application/json"
            );

            Map<String, Object> body = Map.of(
                    "system_instruction", Map.of("parts", List.of(Map.of("text", systemText))),
                    "contents", List.of(Map.of(
                            "role", "user",
                            "parts", List.of(Map.of("text", userText))
                    )),
                    "generation_config", generationConfig
            );

            String uri = String.format("/v1beta/%s:generateContent?key=%s", pathModel, apiKey);

            return webClient
                    .post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .exchangeToMono(resp -> {
                        if (resp.statusCode().is2xxSuccessful()) {
                            return resp.bodyToMono(String.class).map(this::extractTextOrReturnRaw);
                        }
                        return resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(b -> Mono.error(new IllegalStateException(
                                        "Gemini " + resp.statusCode().value() + ": " + b)));
                    });



        } catch (Exception e) {
            return Mono.error(new IllegalStateException("Erro ao montar request para Gemini: " + e.getMessage(), e));
        }
    }

    private String extractTextOrReturnRaw(String rawJson) {
        try {
            JsonNode root = MAPPER.readTree(rawJson);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode textNode = candidates.get(0).path("content").path("parts");
                if (textNode.isArray() && !textNode.isEmpty()) {
                    JsonNode firstPart = textNode.get(0);
                    if (firstPart.has("text")) {
                        return firstPart.get("text").asText();
                    }
                }
            }
            return rawJson;
        } catch (Exception ex) {
            return rawJson;
        }
    }

    private SysUser splitSystemAndUser(String prompt) {
        if (prompt == null || prompt.isBlank()) return new SysUser("", "");
        Pattern p = Pattern.compile("\\[SYSTEM](.*?)\\[USER](.*)", Pattern.DOTALL);
        Matcher m = p.matcher(prompt);
        if (m.find()) {
            String system = m.group(1).trim();
            String user = m.group(2).trim();
            return new SysUser(system.isBlank() ? "Você é um assistente útil." : system, user);
        }
        return new SysUser("Você é um assistente útil.", prompt.trim());
    }

    private record SysUser(String system, String user) {}
}
