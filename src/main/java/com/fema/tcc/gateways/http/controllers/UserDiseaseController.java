package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.domains.disease.UserDisease;
import com.fema.tcc.gateways.http.jsons.UserDiseaseRequestJson;
import com.fema.tcc.gateways.http.jsons.UserDiseaseResponseJson;
import com.fema.tcc.gateways.http.mappers.UserDiseaseJsonMapper;
import com.fema.tcc.usecases.userDisease.CreateUserDiseaseUserCase;
import com.fema.tcc.usecases.userDisease.GetAllUserDiseaseUseCase;
import com.fema.tcc.usecases.userDisease.GetByIdUserDiseaseUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user-diseases")
class UserDiseaseController {

    private final CreateUserDiseaseUserCase createUseCase;
    private final GetByIdUserDiseaseUseCase getUseDiseaseUseCase;
    private final GetAllUserDiseaseUseCase getAllUseCase;
    private final UserDiseaseJsonMapper mapper;

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody UserDiseaseRequestJson request) {

        UserDisease userDiseaseDomain = mapper.requestToDomain(request);
        createUseCase.execute(userDiseaseDomain);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDiseaseResponseJson> getById(@PathVariable Integer id) {

        UserDisease userDisease = getUseDiseaseUseCase.execute(id);
        UserDiseaseResponseJson response = mapper.domainToResponse(userDisease);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserDiseaseResponseJson>> getAll() {

        List<UserDisease> userDiseases = getAllUseCase.execute();
        List<UserDiseaseResponseJson> responseJsons =
                userDiseases.stream().map(mapper::domainToResponse).toList();

        return ResponseEntity.ok(responseJsons);
    }
}
