package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.dietaryRestriction.DietaryRestriction;
import com.fema.tcc.gateways.postgresql.entity.DietaryRestrictionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DietaryRestrictionJsonMapper {

  DietaryRestriction entityToDomain(DietaryRestrictionEntity entity);

  DietaryRestrictionEntity domainToEntity(DietaryRestriction domain);
}
