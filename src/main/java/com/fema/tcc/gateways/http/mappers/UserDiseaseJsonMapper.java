package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.disease.UserDisease;
import com.fema.tcc.gateways.http.jsons.UserDiseaseRequestJson;
import com.fema.tcc.gateways.http.jsons.UserDiseaseResponseJson;
import com.fema.tcc.gateways.postgresql.entity.UserDiseaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {UserJsonMapper.class, DiseaseJsonMapper.class})
public interface UserDiseaseJsonMapper {

  UserDisease entityToDomain(UserDiseaseEntity entity);

  UserDiseaseEntity domainToEntity(UserDisease domain);

  @Mapping(target = "disease.id", source = "diseaseId")
  UserDisease requestToDomain(UserDiseaseRequestJson request);

    UserDiseaseResponseJson domainToResponse(UserDisease domain);
}
