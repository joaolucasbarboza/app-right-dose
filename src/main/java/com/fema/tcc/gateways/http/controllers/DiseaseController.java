package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.domains.disease.Disease;
import com.fema.tcc.gateways.http.jsons.DiseaseResponseJson;
import com.fema.tcc.gateways.http.mappers.DiseaseJsonMapper;
import com.fema.tcc.usecases.disease.DiseaseGetByNameUseCase;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/diseases")
public class DiseaseController {

  private final DiseaseGetByNameUseCase diseaseGetByNameUseCase;
  private final DiseaseJsonMapper mapper;

  @GetMapping
  public ResponseEntity<List<DiseaseResponseJson>> getAll() {
    List<Disease> diseases = diseaseGetByNameUseCase.execute();
    List<DiseaseResponseJson> responsesJson =
        diseases.stream().map(mapper::domainToResponse).toList();

    return ResponseEntity.ok(responsesJson);
  }
}
