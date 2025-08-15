package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.gateways.postgresql.entity.UserDietaryRestrictionEntity;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {UserJsonMapper.class, DietaryRestrictionJsonMapper.class})
public interface UserDietaryRestrictionJsonMapper {

  UserDietaryRestriction entityToDomain(UserDietaryRestrictionEntity entity);

  UserDietaryRestrictionEntity domainToEntity(UserDietaryRestriction domain);
}
