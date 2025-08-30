package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.disease.Disease;
import com.fema.tcc.gateways.http.jsons.DiseaseResponseJson;
import com.fema.tcc.gateways.postgresql.entity.DiseaseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiseaseJsonMapper {

  Disease entityToDomain(DiseaseEntity entity);

  DiseaseEntity domainToEntity(Disease domain);

  DiseaseResponseJson domainToResponse(Disease domain);
}
