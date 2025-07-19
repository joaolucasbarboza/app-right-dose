package com.fema.tcc.gateways.impl;

import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.gateways.PrescriptionGateway;
import com.fema.tcc.gateways.http.mappers.PrescriptionJsonMapper;
import com.fema.tcc.gateways.postgresql.entity.PrescriptionEntity;
import com.fema.tcc.gateways.postgresql.repository.PrescriptionRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PrescriptionGatewayImpl implements PrescriptionGateway {

  private final PrescriptionJsonMapper prescriptionJsonMapper;
  private final PrescriptionRepository prescriptionRepository;

  @Override
  public Prescription save(Prescription prescription) {

    PrescriptionEntity prescriptionEntity = prescriptionJsonMapper.domainToEntity(prescription);
    prescriptionRepository.save(prescriptionEntity);

    return prescriptionJsonMapper.entityToDomain(prescriptionEntity);
  }

  @Override
  public List<Prescription> findAllByUserId(Integer userId) {
    List<PrescriptionEntity> prescriptionEntities =
        prescriptionRepository.findAllByUser_UserId(userId);
    return prescriptionEntities.stream().map(prescriptionJsonMapper::entityToDomain).toList();
  }

  @Override
  public Optional<Prescription> findById(Long id) {
    return prescriptionRepository.findById(id).map(prescriptionJsonMapper::entityToDomain);
  }

  @Override
  public void deleteById(Long id) {
    prescriptionRepository.deleteById(id);
  }
}
