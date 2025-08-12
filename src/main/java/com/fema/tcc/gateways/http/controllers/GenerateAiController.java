package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.usecases.recommendationAi.FoodRecommendationAIUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/generateAi")
public class GenerateAiController {

  private final FoodRecommendationAIUseCase foodRecommendationAIUseCase;

  @GetMapping
  public ResponseEntity<String> generateAi() {
    String response = foodRecommendationAIUseCase.simpleResponse();

    return ResponseEntity.ok(response);
  }
}
