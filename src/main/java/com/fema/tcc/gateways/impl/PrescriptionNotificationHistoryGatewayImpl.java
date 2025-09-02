package com.fema.tcc.gateways.impl;

import com.fema.tcc.domains.prescriptionNotificationHistory.PrescriptionNotificationHistory;
import com.fema.tcc.gateways.PrescriptionNotificationHistoryGateway;
import com.fema.tcc.gateways.http.mappers.PrescriptionNotificationHistoryJsonMapper;
import com.fema.tcc.gateways.postgresql.entity.PrescriptionNotificationHistoryEntity;
import com.fema.tcc.gateways.postgresql.repository.PrescriptionNotificationHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PrescriptionNotificationHistoryGatewayImpl
    implements PrescriptionNotificationHistoryGateway {

  private final PrescriptionNotificationHistoryRepository repository;
  private final PrescriptionNotificationHistoryJsonMapper jsonMapper;

  @Override
  public void save(PrescriptionNotificationHistory notificationHistory) {
    PrescriptionNotificationHistoryEntity entity = jsonMapper.domainToEntity(notificationHistory);
    repository.save(entity);
  }

    @Override
    public Long countStatusConfirmedByPrescriptionId(Long id) {
        return repository.countConfirmed(id);
    }
}
