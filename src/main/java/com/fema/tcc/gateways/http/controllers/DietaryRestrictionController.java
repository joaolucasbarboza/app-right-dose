package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.domains.dietaryRestriction.DietaryRestriction;
import com.fema.tcc.gateways.http.mappers.DietaryRestrictionJsonMapper;
import com.fema.tcc.gateways.http.mappers.DietaryRestrictionResponseJson;
import com.fema.tcc.usecases.dietaryRestriction.DietaryRestrictionGetAllUseCase;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/dietaries")
public class DietaryRestrictionController {

  private final DietaryRestrictionJsonMapper mapper;
  private final DietaryRestrictionGetAllUseCase dietaryRestrictionGetAllUseCase;

  @GetMapping
  public ResponseEntity<List<DietaryRestrictionResponseJson>> getAll() {

    List<DietaryRestriction> dietaryRestrictions = dietaryRestrictionGetAllUseCase.execute();
    List<DietaryRestrictionResponseJson> responsesJson =
        dietaryRestrictions.stream().map(mapper::domainToResponse).toList();

    return ResponseEntity.ok(responsesJson);
  }
}
