package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.domains.medicine.Medicine;
import com.fema.tcc.gateways.http.json.MedicineRequestJson;
import com.fema.tcc.gateways.http.json.MedicineResponseJson;
import com.fema.tcc.gateways.http.mappers.MedicineJsonMapper;
import com.fema.tcc.usecases.Medicine.MedicineUseCase;
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

  @PostMapping
  public ResponseEntity<MedicineResponseJson> create(
      @RequestBody @Valid MedicineRequestJson medicineRequestJson) {

    Medicine medicine =
        medicineUseCase.create(medicineJsonMapper.requestToDomain(medicineRequestJson));
    MedicineResponseJson responseJson = medicineJsonMapper.domainToResponse(medicine);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
  }

  @GetMapping
  public ResponseEntity<List<MedicineResponseJson>> getMedicines() {

    List<MedicineResponseJson> medicineResponseList =
        medicineJsonMapper.domainListToResponseList(medicineUseCase.getMedicines());

    return ResponseEntity.status(HttpStatus.OK).body(medicineResponseList);
  }

  @GetMapping("/{medicineId}")
  public ResponseEntity<MedicineResponseJson> getMedicineById(@PathVariable Integer medicineId) {

    Medicine medicine = medicineUseCase.getMedicineById(medicineId);
    MedicineResponseJson responseJson = medicineJsonMapper.domainToResponse(medicine);

    return ResponseEntity.status(HttpStatus.OK).body(responseJson);
  }

  @PutMapping("/{medicineId}")
  public ResponseEntity<MedicineResponseJson> update(
      @PathVariable Integer medicineId,
      @RequestBody @Valid MedicineRequestJson medicineRequestJson) {

    Medicine medicine = medicineJsonMapper.requestToDomain(medicineRequestJson);
    medicine.setId(medicineId);

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
