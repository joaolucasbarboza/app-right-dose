package com.fema.tcc.usecases.recommendationAi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.gateways.http.jsons.BuildDetailsPrescriptions;
import com.fema.tcc.gateways.http.jsons.FoodRecommendationResponseJson;
import com.fema.tcc.usecases.prescription.GetAllPrescriptionUseCase;
import com.fema.tcc.usecases.userDietaryRestriction.GetAllUserDietaryRestrictionUseCase;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class RecommendationService {

  private final GeminiClient client;
  private final GetAllPrescriptionUseCase getAllPrescriptionUseCase;
  private final GetAllUserDietaryRestrictionUseCase getAllUserDietaryRestrictionUseCase;

  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public Mono<FoodRecommendationResponseJson> execute() {
    final List<Prescription> prescriptions =
        Optional.ofNullable(getAllPrescriptionUseCase.execute()).orElse(List.of());

    final List<UserDietaryRestriction> restrictions =
        Optional.ofNullable(getAllUserDietaryRestrictionUseCase.execute()).orElse(List.of());

    final List<BuildDetailsPrescriptions> details =
        prescriptions.stream()
            .filter(Objects::nonNull)
            .map(
                p ->
                    BuildDetailsPrescriptions.builder()
                        .name(p.getMedicine() != null ? p.getMedicine().getName() : null)
                        .description(
                            p.getMedicine() != null ? p.getMedicine().getDescription() : null)
                        .dosageAmount(p.getDosageAmount())
                        .dosageUnit(p.getDosageUnit())
                        .frequency(p.getFrequency())
                        .uomFrequency(p.getUomFrequency())
                        .indefinite(p.isIndefinite())
                        .totalOccurrences(p.getTotalOccurrences())
                        .startDate(p.getStartDate())
                        .endDate(p.getEndDate())
                        .instructions(p.getInstructions())
                        .build())
            .toList();

    try {
      final String prescriptionsJson = MAPPER.writeValueAsString(details);
      final String restrictionsJson = MAPPER.writeValueAsString(restrictions);

      final Map<String, Object> req = buildGenerateRequest(prescriptionsJson, restrictionsJson);

      return client.chat(req).flatMap(this::parseFromRaw);

    } catch (Exception e) {
      return Mono.error(
          new IllegalStateException("Erro ao serializar dados para JSON: " + e.getMessage(), e));
    }
  }

  @NotNull
  private static Map<String, Object> buildGenerateRequest(
      String prescriptionsJson, String restrictionsJson) {
    final String system =
        """
        Você é uma assistente de saúde. Gere recomendações alimentares simples e aponte possíveis interações/alertas.
        NÃO faça diagnóstico nem prescrição.

        Responda APENAS em JSON válido. Regras ESTRITAS de formato:
        - "meals": cada campo deve ser UMA STRING simples (sem arrays, sem objetos).
        - "alerts": ARRAY de STRINGS, onde cada item descreve um risco alimentar claramente, com base na doença do usuário, nas medicações e nas restrições alimentares.
          Formato sugerido: "Risco: <alimento/ingrediente>. Motivo: <interação/condição/restrição>."
        - "substitutions": ARRAY de STRINGS, onde cada item descreve uma substituição prática.
          Formato sugerido: "Substitua <alimento A> por <alimento B> porque <motivo relacionado à doença/medicação/restrição>."
        - PROIBIDO retornar objetos/arrays dentro de strings (nada de JSON-encapsulado).
        - PROIBIDO retornar objetos em "alerts" ou "substitutions": devem ser STRINGS simples.
        """;

    final String user =
        """
        Considere os dados do usuário:

        PRESCRIPTIONS_JSON:
        %s

        RESTRICTIONS_JSON:
        %s

        Gere o JSON com os campos:
        - model (string)
        - generatedAt (ISO-8601 UTC)
        - meals { breakfast, lunch, dinner, snackMorning, snackAfternoon } (todas STRINGS simples)
        - alerts (array de STRINGS simples, 2 a 4 itens)
        - substitutions (array de STRINGS simples, 2 a 4 itens)
        - profileApplied (objeto livre, opcional)

        Importante:
        - Escreva em pt-BR.
        - NÃO retorne JSON dentro de strings.
        - Se precisar listar itens dentro de uma string, separe por vírgulas simples.
        """
            .formatted(prescriptionsJson, restrictionsJson);

    final String prompt =
        """
        [SYSTEM]
        %s

        [USER]
        %s
        """
            .formatted(system, user);

    return Map.of(
        "targetModel",
        "gemini-2.5-flash",
        "prompt",
        prompt,
        "stream",
        false,
        "format",
        "json",
        "options",
        Map.of("temperature", 0.2));
  }

  private Mono<FoodRecommendationResponseJson> parseFromRaw(String raw) {
    try {
      JsonNode root = MAPPER.readTree(raw);

      JsonNode responseNode = root.get("response");
      JsonNode messageNode = root.path("message").get("content");
      boolean isDirectFinal = root.has("meals") || root.has("alerts") || root.has("substitutions");

      JsonNode contentNode = null;

      if (responseNode != null && !responseNode.isNull()) {
        contentNode = parseStringOrNode(responseNode);
      } else if (messageNode != null && !messageNode.isNull()) {
        contentNode = parseStringOrNode(messageNode);
      } else if (isDirectFinal) {
        contentNode = root;
      } else {
        throw new IllegalStateException(
            "Resposta do Ollama sem 'response', sem 'message.content' e sem campos finais.");
      }

      if (contentNode.isTextual()) {
        contentNode = tryParseJsonOrWrapAsText(contentNode.asText());
      }

      String model = optText(contentNode.get("model"));
      if (model == null) {
        model = optText(root.get("model"));
      }

      OffsetDateTime generatedAt = OffsetDateTime.now();
      String gen = optText(contentNode.get("generatedAt"));
      if (gen != null) {
        try {
          generatedAt = OffsetDateTime.parse(gen);
        } catch (Exception ignored) {
        }
      }

      JsonNode mealsNode = contentNode.get("meals");
      String breakfast = asTextOrJson(mealsNode != null ? mealsNode.get("breakfast") : null);
      String lunch = asTextOrJson(mealsNode != null ? mealsNode.get("lunch") : null);
      String dinner = asTextOrJson(mealsNode != null ? mealsNode.get("dinner") : null);
      String snackMorning = asTextOrJson(mealsNode != null ? mealsNode.get("snackMorning") : null);
      String snackAfternoon =
          asTextOrJson(mealsNode != null ? mealsNode.get("snackAfternoon") : null);

      var meals =
          new FoodRecommendationResponseJson.Meals(
              breakfast, lunch, dinner, snackMorning, snackAfternoon);

      List<String> alerts = toStringList(contentNode.get("alerts"));
      List<String> substitutions = toStringList(contentNode.get("substitutions"));

      Map<String, Object> profile = null;
      JsonNode profileNode = contentNode.get("profileApplied");
      if (profileNode != null && !profileNode.isNull()) {
        profile =
            MAPPER.convertValue(
                profileNode,
                MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
      }

      return Mono.just(
          new FoodRecommendationResponseJson(
              model, generatedAt, meals, alerts, substitutions, profile));

    } catch (Exception e) {
      return Mono.error(
          new IllegalStateException("Falha ao parsear JSON do LLM: " + e.getMessage(), e));
    }
  }

  private static JsonNode parseStringOrNode(JsonNode n) {
    if (n.isTextual()) {
      return tryParseJsonOrWrapAsText(n.asText());
    }
    return n;
  }

  private static JsonNode tryParseJsonOrWrapAsText(String s) {
    try {
      JsonNode n = MAPPER.readTree(s);
      if (!n.isObject()) {
        return MAPPER.createObjectNode().put("text", s);
      }
      return n;
    } catch (Exception ex) {
      return MAPPER.createObjectNode().put("text", s);
    }
  }

  private static String optText(JsonNode node) {
    return (node != null && node.isTextual()) ? node.asText() : null;
  }

  private static String asTextOrJson(JsonNode node) {
    if (node == null || node.isNull()) return null;
    if (node.isTextual()) return node.asText();
    try {
      return MAPPER.writeValueAsString(node);
    } catch (Exception e) {
      return node.toString();
    }
  }

  private static List<String> toStringList(JsonNode node) {
    List<String> out = new ArrayList<>();
    if (node == null || node.isNull()) return out;
    if (node.isArray()) {
      node.forEach(n -> out.add(asTextOrJson(n)));
    } else {
      out.add(asTextOrJson(node));
    }
    return out;
  }
}
