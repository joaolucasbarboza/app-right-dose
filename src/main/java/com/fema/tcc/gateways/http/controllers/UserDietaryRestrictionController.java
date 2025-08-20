package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.gateways.http.jsons.UserDietaryRestrictionRequestJson;
import com.fema.tcc.gateways.http.mappers.UserDietaryRestrictionJsonMapper;
import com.fema.tcc.usecases.userDietaryRestriction.CreateUserDietaryRestrictionsUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/user-dietary-restrictions")
class UserDietaryRestrictionController {

  private final CreateUserDietaryRestrictionsUseCase createUseCase;
  private final UserDietaryRestrictionJsonMapper mapper;

  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @RequestBody UserDietaryRestrictionRequestJson requestJson) {
    UserDietaryRestriction domain = mapper.requestToDomain(requestJson);
    createUseCase.execute(domain);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
