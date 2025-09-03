package com.fema.tcc.gateways.impl;

import com.fema.tcc.domains.medicine.Medicine;
import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.MedicineGateway;
import com.fema.tcc.gateways.http.mappers.MedicineJsonMapper;
import com.fema.tcc.gateways.http.mappers.UserJsonMapper;
import com.fema.tcc.gateways.postgresql.entity.MedicineEntity;
import com.fema.tcc.gateways.postgresql.entity.UserEntity;
import com.fema.tcc.gateways.postgresql.repository.MedicineRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MedicineGatewayImpl implements MedicineGateway {

  private final MedicineRepository medicineRepository;
  private final MedicineJsonMapper medicineJsonMapper;
  private final UserJsonMapper userJsonMapper;

  @Override
  public Medicine save(Medicine medicine) {

    MedicineEntity medicineEntity = medicineJsonMapper.domainToEntity(medicine);
    medicineRepository.save(medicineEntity);

    return medicineJsonMapper.entityToDomain(medicineEntity);
  }

  @Override
  public void deleteMedicine(Medicine medicine) {
    medicineRepository.deleteById(medicine.getId());
  }

  @Override
  public List<Medicine> findMedicinesByUserId(User user) {

    UserEntity userEntity = userJsonMapper.domainToEntity(user);
    List<MedicineEntity> medicineEntities = medicineRepository.findByUser(userEntity);

    return medicineEntities.stream()
        .map(medicineJsonMapper::entityToDomain)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Medicine> findById(Integer medicineId) {
    return medicineRepository.findById(medicineId).map(medicineJsonMapper::entityToDomain);
  }
}
