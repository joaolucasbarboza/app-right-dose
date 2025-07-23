package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.gateways.http.jsons.PrescriptionRequestJson;
import com.fema.tcc.gateways.http.jsons.PrescriptionResponseJson;
import com.fema.tcc.gateways.http.jsons.PrescriptionWithNotificationsResponseJson;
import com.fema.tcc.gateways.postgresql.entity.PrescriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrescriptionJsonMapper {

  PrescriptionEntity domainToEntity(Prescription prescription);

  Prescription entityToDomain(PrescriptionEntity prescriptionEntity);

  @Mapping(
      target = "medicine",
      expression = "java(Medicine.builder().id(request.medicineId()).build())")
  Prescription requestToDomain(PrescriptionRequestJson request);

  PrescriptionResponseJson domainToResponse(Prescription prescription);

  PrescriptionWithNotificationsResponseJson toResponseWithNotifications(Prescription prescription);
}
