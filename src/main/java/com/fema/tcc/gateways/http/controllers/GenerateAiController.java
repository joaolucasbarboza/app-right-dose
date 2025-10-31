package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.gateways.http.jsons.FoodRecommendationResponseJson;
import com.fema.tcc.usecases.recommendationai.RecommendationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/generate-ai")
public class GenerateAiController {

  private final RecommendationService recommendationService;

  @GetMapping(produces = "application/json;charset=UTF-8")
  public ResponseEntity<FoodRecommendationResponseJson> generateAiMvc() {
    log.info("Generating AI recommendation");

    FoodRecommendationResponseJson body = recommendationService.execute().block();
    return ResponseEntity.ok(body);
  }
}
