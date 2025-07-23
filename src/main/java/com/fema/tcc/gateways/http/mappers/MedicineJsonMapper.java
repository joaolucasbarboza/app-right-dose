package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.medicine.Medicine;
import com.fema.tcc.gateways.http.jsons.MedicineRequestJson;
import com.fema.tcc.gateways.http.jsons.MedicineResponseJson;
import com.fema.tcc.gateways.postgresql.entity.MedicineEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicineJsonMapper {

  Medicine requestToDomain(MedicineRequestJson medicineRequestJson);

  MedicineResponseJson domainToResponse(Medicine medicine);

  MedicineEntity domainToEntity(Medicine medicine);

  Medicine entityToDomain(MedicineEntity medicineEntity);

  List<MedicineResponseJson> domainToResponseList(List<Medicine> medicineList);
}
