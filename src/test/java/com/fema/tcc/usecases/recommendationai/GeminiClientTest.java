package com.fema.tcc.usecases.recommendationai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GeminiClientTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private MockWebServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    private GeminiClient newClient(String defaultModel) {
        String baseUrl = server.url("/").toString(); // ex: http://127.0.0.1:NNNN/
        return new GeminiClient(baseUrl, "API_KEY", defaultModel);
    }

    @Test
    void chat_success_extractsText_andBuildsBodyWithSystemUser_andCustomTemperature() throws Exception {
        // Resposta simulada contendo candidates[0].content.parts[0].text
        String responseJson = """
            {
              "candidates": [
                {
                  "content": {
                    "parts": [
                      {"text": "Olá do Gemini!"}
                    ]
                  }
                }
              ]
            }
            """;
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(responseJson));

        GeminiClient client = newClient("gemini-1.5-flash");

        Map<String, Object> payload = new HashMap<>();
        payload.put("prompt", "[SYSTEM] Você é um assistente útil. [USER] Me dê um oi.");
        payload.put("options", Map.of("temperature", 0.7));

        StepVerifier.create(client.chat(payload))
                .expectNext("Olá do Gemini!")
                .verifyComplete();

        // Verifica path e query-string
        RecordedRequest req = server.takeRequest();
        assertEquals("/v1beta/models/gemini-1.5-flash:generateContent?key=API_KEY", req.getPath());
        assertEquals("POST", req.getMethod());
        assertEquals("application/json", req.getHeader("Content-Type"));

        // Verifica JSON enviado
        JsonNode sent = MAPPER.readTree(req.getBody().readUtf8());
        String systemText = sent.path("system_instruction").path("parts").get(0).path("text").asText();
        String userText = sent.path("contents").get(0).path("parts").get(0).path("text").asText();
        double temperature = sent.path("generation_config").path("temperature").asDouble();

        assertEquals("Você é um assistente útil.", systemText);
        assertEquals("Me dê um oi.", userText);
        assertEquals(0.7, temperature, 1e-9);
    }

    @Test
    void chat_returnsRawJson_whenNoTextToExtract() {
        // Sem candidates -> deve devolver o JSON bruto
        String responseJson = "{}";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(responseJson));

        GeminiClient client = newClient("gemini-1.5-flash");

        Map<String, Object> payload = Map.of("prompt", "sem partes de texto");
        StepVerifier.create(client.chat(payload))
                .expectNext("{}")
                .verifyComplete();
    }

    @Test
    void chat_non2xx_returnsErroMonoWithStatusAndBody() {
        String err = """
            {"error":{"code":404,"message":"not found","status":"NOT_FOUND"}}
            """;
        server.enqueue(new MockResponse()
                .setResponseCode(404)
                .setHeader("Content-Type", "application/json")
                .setBody(err));

        GeminiClient client = newClient("gemini-1.5-flash");

        StepVerifier.create(client.chat(Map.of("prompt", "qualquer")))
                .expectErrorSatisfies(th -> {
                    assertInstanceOf(IllegalStateException.class, th);
                    String msg = th.getMessage();
                    assertNotNull(msg);
                    assertTrue(msg.contains("Gemini 404:"), "deveria conter o prefixo do erro");
                    assertTrue(msg.contains("\"status\":\"NOT_FOUND\""), "deveria conter o corpo retornado");
                })
                .verify();
    }

    @Test
    void chat_usesDefaultTemperature_whenOptionsMissing() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("""
                    {"candidates":[{"content":{"parts":[{"text":"ok"}]}}]}
                """));

        GeminiClient client = newClient("gemini-1.5-flash");

        StepVerifier.create(client.chat(Map.of("prompt", "hello")))
                .expectNext("ok")
                .verifyComplete();

        RecordedRequest req = server.takeRequest();
        JsonNode sent = MAPPER.readTree(req.getBody().readUtf8());
        double temperature = sent.path("generation_config").path("temperature").asDouble();
        assertEquals(0.2, temperature, 1e-9);
    }

    @Test
    void chat_respectsTargetModel_withoutModelsPrefix() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("""
                    {"candidates":[{"content":{"parts":[{"text":"modelo sem prefixo"}]}}]}
                """));

        GeminiClient client = newClient("gemini-1.5-pro"); // default
        Map<String, Object> payload = new HashMap<>();
        payload.put("prompt", "teste");
        payload.put("targetModel", "gemini-1.5-flash"); // sem "models/"

        StepVerifier.create(client.chat(payload))
                .expectNext("modelo sem prefixo")
                .verifyComplete();

        RecordedRequest req = server.takeRequest();
        assertEquals("/v1beta/models/gemini-1.5-flash:generateContent?key=API_KEY", req.getPath());
    }

    @Test
    void chat_respectsTargetModel_withModelsPrefix() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("""
                    {"candidates":[{"content":{"parts":[{"text":"modelo com prefixo"}]}}]}
                """));

        GeminiClient client = newClient("gemini-1.5-pro"); // default
        Map<String, Object> payload = new HashMap<>();
        payload.put("prompt", "teste");
        payload.put("targetModel", "models/custom-model");

        StepVerifier.create(client.chat(payload))
                .expectNext("modelo com prefixo")
                .verifyComplete();

        RecordedRequest req = server.takeRequest();
        assertEquals("/v1beta/models/custom-model:generateContent?key=API_KEY", req.getPath());
    }

    @Test
    void chat_handlesBlankPrompt_buildsEmptySystemAndUser() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("""
                    {"candidates":[{"content":{"parts":[{"text":"ok"}]}}]}
                """));

        GeminiClient client = newClient("gemini-1.5-flash");

        StepVerifier.create(client.chat(Map.of("prompt", "  ")))
                .expectNext("ok")
                .verifyComplete();

        RecordedRequest req = server.takeRequest();
        JsonNode sent = MAPPER.readTree(req.getBody().readUtf8());

        assertEquals("", sent.path("system_instruction").path("parts").get(0).path("text").asText());
        assertEquals("", sent.path("contents").get(0).path("parts").get(0).path("text").asText());
    }
}
