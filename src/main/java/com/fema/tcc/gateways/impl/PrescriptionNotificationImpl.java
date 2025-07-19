package com.fema.tcc.gateways.impl;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import com.fema.tcc.gateways.http.exceptions.NotFoundException;
import com.fema.tcc.gateways.http.mappers.PrescriptionNotificationJsonMapper;
import com.fema.tcc.gateways.postgresql.entity.PrescriptionNotificationEntity;
import com.fema.tcc.gateways.postgresql.repository.PrescriptionNotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PrescriptionNotificationImpl implements PrescriptionNotificationGateway {

  private final PrescriptionNotificationRepository repository;
  private final PrescriptionNotificationJsonMapper jsonMapper;

  @Override
  public void saveAll(List<PrescriptionNotification> prescriptions) {
    List<PrescriptionNotificationEntity> entities =
        prescriptions.stream().map(jsonMapper::domainToEntity).toList();
    repository.saveAll(entities);
  }

  @Override
  public void update(PrescriptionNotification prescriptionNotification) {
    PrescriptionNotificationEntity entity = jsonMapper.domainToEntity(prescriptionNotification);
    repository.save(entity);
  }

  @Override
  public PrescriptionNotification findById(Long id) {
    PrescriptionNotificationEntity entity =
        repository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException("Not found PrescriptionNotification with id: " + id));
    return jsonMapper.entityToDomain(entity);
  }

  @Override
  public List<PrescriptionNotification> findAllReadyToNotify(
      LocalDateTime now, LocalDateTime limitTime) {
    return repository.findAllReadyToNotify(now, limitTime).stream()
        .map(jsonMapper::entityToDomain)
        .toList();
  }
}
