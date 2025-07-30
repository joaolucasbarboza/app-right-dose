package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.prescriptionNotificationHistory.PrescriptionNotificationHistory;
import com.fema.tcc.gateways.postgresql.entity.PrescriptionNotificationHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionNotificationHistoryJsonMapper {

    PrescriptionNotificationHistoryEntity domainToEntity(
            PrescriptionNotificationHistory prescriptionNotificationHistory);
}
