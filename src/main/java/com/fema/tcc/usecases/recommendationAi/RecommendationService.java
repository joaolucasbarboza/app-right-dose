package com.fema.tcc.usecases.recommendationAi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.gateways.http.jsons.BuildDetailsPrescriptions;
import com.fema.tcc.gateways.http.jsons.FoodRecommendationResponseJson;
import com.fema.tcc.gateways.http.jsons.OllamaChatResponse;
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

  private final OllamaClient client;
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

      final Map<String, Object> req = getStringObjectMap(prescriptionsJson, restrictionsJson);

      return client.chat(req).flatMap(this::parseMinimal);

    } catch (Exception e) {
      return Mono.error(
          new IllegalStateException("Erro ao serializar dados para JSON: " + e.getMessage(), e));
    }
  }

  @NotNull
  private static Map<String, Object> getStringObjectMap(
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
  - alerts (array de STRINGS simples, 2 a 4 itens) -> cada item deve citar o alimento/ingrediente de risco e o MOTIVO com base em doença/medicações/restrições
  - substitutions (array de STRINGS simples, 2 a 4 itens) -> cada item no formato "Substitua X por Y porque ..."
  - profileApplied (objeto livre, opcional)

  Importante:
  - Escreva em pt-BR.
  - NÃO retorne JSON dentro de strings (sem barras invertidas, sem objetos/arrays serializados como texto).
  - Se precisar listar itens dentro de uma string (ex.: na refeição), separe por vírgulas simples.
  """
            .formatted(prescriptionsJson, restrictionsJson);

    return Map.of(
        "model", "llama3",
        "messages",
            List.of(
                Map.of("role", "system", "content", system),
                Map.of("role", "user", "content", user)),
        "stream", false,
        "format", "json",
        "language", "pt-BR");
  }

  private Mono<FoodRecommendationResponseJson> parseMinimal(OllamaChatResponse resp) {
    try {
      var msg = (Map<?, ?>) resp.message();
      var content = (String) msg.get("content");

      // Lê como árvore para normalizar tipos vindos do LLM
      JsonNode root = MAPPER.readTree(content);

      // model / generatedAt (com defaults)
      String model = optText(root.get("model"));
      if (model == null) model = resp.model();

      OffsetDateTime generatedAt = OffsetDateTime.now();
      String gen = optText(root.get("generatedAt"));
      if (gen != null) {
        try {
          generatedAt = OffsetDateTime.parse(gen);
        } catch (Exception ignored) {
        }
      }

      // meals: força tudo para String (se vier objeto/array, serializa em JSON minificado)
      JsonNode mealsNode = root.get("meals");
      String breakfast = asTextOrJson(mealsNode != null ? mealsNode.get("breakfast") : null);
      String lunch = asTextOrJson(mealsNode != null ? mealsNode.get("lunch") : null);
      String dinner = asTextOrJson(mealsNode != null ? mealsNode.get("dinner") : null);
      String snackMorning = asTextOrJson(mealsNode != null ? mealsNode.get("snackMorning") : null);
      String snackAfternoon =
          asTextOrJson(mealsNode != null ? mealsNode.get("snackAfternoon") : null);

      var meals =
          new FoodRecommendationResponseJson.Meals(
              breakfast, lunch, dinner, snackMorning, snackAfternoon);

      // alerts / substitutions: normaliza para List<String>
      List<String> alerts = toStringList(root.get("alerts"));
      List<String> substitutions = toStringList(root.get("substitutions"));

      // profileApplied: mantém como Map<String,Object> se existir
      Map<String, Object> profile = null;
      JsonNode profileNode = root.get("profileApplied");
      if (profileNode != null && !profileNode.isNull()) {
        profile =
            MAPPER.convertValue(
                profileNode,
                MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
      }

      var finalResp =
          new FoodRecommendationResponseJson(
              model, generatedAt, meals, alerts, substitutions, profile);
      return Mono.just(finalResp);

    } catch (Exception e) {
      return Mono.error(
          new IllegalStateException("Falha ao parsear JSON do LLM: " + e.getMessage(), e));
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
      // Se vier single objeto/string, vira lista unitária
      out.add(asTextOrJson(node));
    }
    return out;
  }
}
