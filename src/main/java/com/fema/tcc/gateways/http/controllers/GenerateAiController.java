package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.gateways.http.jsons.FoodRecommendationResponseJson;
import com.fema.tcc.useCases.recommendationAi.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/generate-ai")
public class GenerateAiController {

  private final RecommendationService recommendationService;

  @GetMapping(produces = "application/json;charset=UTF-8")
  public ResponseEntity<FoodRecommendationResponseJson> generateAiMvc() {
    FoodRecommendationResponseJson body = recommendationService.execute().block();
    return ResponseEntity.ok(body);
  }
}
