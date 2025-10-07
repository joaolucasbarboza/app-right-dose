package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.gateways.http.jsons.FcmTokenRequest;
import com.fema.tcc.gateways.http.jsons.UserResponseJson;
import com.fema.tcc.gateways.http.mappers.UserJsonMapper;
import com.fema.tcc.usecases.user.UserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "user")
@AllArgsConstructor
public class UserController {

  private final UserUseCase userUseCase;
  private final UserJsonMapper userJsonMapper;

  @GetMapping
  public ResponseEntity<UserResponseJson> getById() {

    UserResponseJson response = userJsonMapper.domainToResponse(userUseCase.getUser());

    return ResponseEntity.ok().body(response);
  }

  @PutMapping("/update-token")
  public ResponseEntity<UserResponseJson> updateToken(@RequestBody FcmTokenRequest tokenRequest) {

    UserResponseJson response =
        userJsonMapper.domainToResponse(userUseCase.updateToken(tokenRequest));

    return ResponseEntity.ok().body(response);
  }
}
