package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.gateways.http.jsons.UserDietaryRestrictionRequestJson;
import com.fema.tcc.gateways.postgresql.entity.UserDietaryRestrictionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {UserJsonMapper.class, DietaryRestrictionJsonMapper.class})
public interface UserDietaryRestrictionJsonMapper {

  @Mapping(target = "dietaryRestriction.id", source = "dietaryRestrictionId")
  UserDietaryRestriction requestToDomain(UserDietaryRestrictionRequestJson request);

  UserDietaryRestriction entityToDomain(UserDietaryRestrictionEntity entity);

  UserDietaryRestrictionEntity domainToEntity(UserDietaryRestriction domain);
}
