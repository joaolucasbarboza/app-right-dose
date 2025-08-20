package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.gateways.http.jsons.UserDietaryRestrictionRequestJson;
import com.fema.tcc.gateways.http.jsons.UserDietaryRestrictionResponseJson;
import com.fema.tcc.gateways.http.mappers.UserDietaryRestrictionJsonMapper;
import com.fema.tcc.usecases.userDietaryRestriction.CreateUserDietaryRestrictionsUseCase;
import com.fema.tcc.usecases.userDietaryRestriction.GetByIdUserDietaryRestrictionUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/user-dietary-restrictions")
class UserDietaryRestrictionController {

  private final UserDietaryRestrictionJsonMapper mapper;
  private final CreateUserDietaryRestrictionsUseCase createUseCase;
  private final GetByIdUserDietaryRestrictionUseCase getByIdUseCase;

  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @RequestBody UserDietaryRestrictionRequestJson requestJson) {
    UserDietaryRestriction domain = mapper.requestToDomain(requestJson);

    createUseCase.execute(domain);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDietaryRestrictionResponseJson> getById(@PathVariable Integer id) {
    UserDietaryRestriction userDietaryRestriction = getByIdUseCase.execute(id);

    UserDietaryRestrictionResponseJson responseJson =
        mapper.domainToResponse(userDietaryRestriction);

    return ResponseEntity.ok(responseJson);
  }
}
