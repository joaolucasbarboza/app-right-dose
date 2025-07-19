package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.medicine.Medicine;
import com.fema.tcc.gateways.http.jsons.MedicineRequestJson;
import com.fema.tcc.gateways.http.jsons.MedicineResponseJson;
import com.fema.tcc.gateways.postgresql.entity.MedicineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicineJsonMapper {

    MedicineJsonMapper INSTANCE = Mappers.getMapper(MedicineJsonMapper.class);

    Medicine requestToDomain(MedicineRequestJson medicineRequestJson);

    MedicineResponseJson domainToResponse(Medicine medicine);

    MedicineEntity domainToEntity(Medicine medicine);

    Medicine entityToDomain(MedicineEntity medicineEntity);

    List<MedicineResponseJson> domainToResponseList(List<Medicine> medicineList);
}
