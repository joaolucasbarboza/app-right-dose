package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.http.jsons.PrescriptionNotificationResponseJson;
import com.fema.tcc.gateways.http.jsons.PrescriptionUpdateNotificationRequestJson;
import com.fema.tcc.gateways.postgresql.entity.PrescriptionNotificationEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PrescriptionJsonMapper.class)
public interface PrescriptionNotificationJsonMapper {

  PrescriptionNotificationEntity domainToEntity(PrescriptionNotification prescriptionNotification);

  List<PrescriptionNotificationEntity> domainToEntityList(
      List<PrescriptionNotification> prescriptionNotifications);

  PrescriptionNotification entityToDomain(PrescriptionNotificationEntity entity);

  PrescriptionNotification requestUpdateStatusToDomain(
      PrescriptionUpdateNotificationRequestJson request);

  @Mapping(target = "prescriptionId", source = "prescription.id")
  PrescriptionNotificationResponseJson domainToResponse(
      PrescriptionNotification prescriptionNotification);
}
