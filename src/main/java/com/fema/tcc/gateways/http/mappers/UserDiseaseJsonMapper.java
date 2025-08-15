package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.disease.UserDisease;
import com.fema.tcc.gateways.postgresql.entity.UserDiseaseEntity;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {UserJsonMapper.class, DiseaseJsonMapper.class})
public interface UserDiseaseJsonMapper {

  UserDisease entityToDomain(UserDiseaseEntity entity);

  UserDiseaseEntity domainToEntity(UserDisease domain);
}
