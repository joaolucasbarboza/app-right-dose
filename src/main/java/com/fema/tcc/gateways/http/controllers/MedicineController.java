package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.domains.medicine.Medicine;
import com.fema.tcc.gateways.http.jsons.MedicineRequestJson;
import com.fema.tcc.gateways.http.jsons.MedicineResponseJson;
import com.fema.tcc.gateways.http.mappers.MedicineJsonMapper;
import com.fema.tcc.usecases.medicine.MedicineUseCase;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "medicine")
public class MedicineController {

  private final MedicineUseCase medicineUseCase;
  private final MedicineJsonMapper medicineJsonMapper;

  @Autowired
  public MedicineController(
      MedicineUseCase medicineUseCase, MedicineJsonMapper medicineJsonMapper) {
    this.medicineUseCase = medicineUseCase;
    this.medicineJsonMapper = medicineJsonMapper;
  }

  @PostMapping(produces = "application/json;charset=UTF-8")
  public ResponseEntity<MedicineResponseJson> create(
      @RequestBody @Valid MedicineRequestJson medicineRequestJson) {

    Medicine request = medicineJsonMapper.requestToDomain(medicineRequestJson);

    Medicine medicine =
        medicineUseCase.create(request);
    MedicineResponseJson responseJson = medicineJsonMapper.domainToResponse(medicine);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
  }

  @GetMapping(produces = "application/json;charset=UTF-8")
  public ResponseEntity<List<MedicineResponseJson>> getMedicines() {

    List<MedicineResponseJson> medicineResponseList =
        medicineJsonMapper.domainToResponseList(medicineUseCase.getMedicines());

    return ResponseEntity.status(HttpStatus.OK).body(medicineResponseList);
  }

  @GetMapping(value = "/{medicineId}", produces = "application/json;charset=UTF-8")
  public ResponseEntity<MedicineResponseJson> getMedicineById(@PathVariable Integer medicineId) {

    Medicine medicine = medicineUseCase.getMedicineById(medicineId);
    MedicineResponseJson responseJson = medicineJsonMapper.domainToResponse(medicine);

    return ResponseEntity.status(HttpStatus.OK).body(responseJson);
  }

  @PutMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
  public ResponseEntity<MedicineResponseJson> update(
      @PathVariable Integer id,
      @RequestBody @Valid MedicineRequestJson medicineRequestJson) {

    Medicine medicine = medicineJsonMapper.requestToDomain(medicineRequestJson);
    medicine.setId(id);

    MedicineResponseJson responseJson =
        medicineJsonMapper.domainToResponse(medicineUseCase.updateMedicine(medicine));

    return ResponseEntity.status(HttpStatus.OK).body(responseJson);
  }

  @DeleteMapping("/{medicineId}")
  public ResponseEntity<?> delete(@PathVariable Integer medicineId) {

    medicineUseCase.deleteMedicine(medicineId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
