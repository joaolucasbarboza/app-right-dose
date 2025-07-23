package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.postgresql.entity.PrescriptionNotificationEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = PrescriptionJsonMapper.class)
public interface PrescriptionNotificationJsonMapper {

  PrescriptionNotificationEntity domainToEntity(PrescriptionNotification prescriptionNotification);

  List<PrescriptionNotificationEntity> domainToEntityList(
      List<PrescriptionNotification> prescriptionNotifications);

  PrescriptionNotification entityToDomain(PrescriptionNotificationEntity entity);
}
