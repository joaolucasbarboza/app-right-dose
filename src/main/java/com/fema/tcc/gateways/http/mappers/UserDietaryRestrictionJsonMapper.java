package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.gateways.http.jsons.UserDietaryRestrictionRequestJson;
import com.fema.tcc.gateways.http.jsons.UserDietaryRestrictionResponseJson;
import com.fema.tcc.gateways.postgresql.entity.UserDietaryRestrictionEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {UserJsonMapper.class, DietaryRestrictionJsonMapper.class})
public interface UserDietaryRestrictionJsonMapper {

  UserDietaryRestriction entityToDomain(UserDietaryRestrictionEntity entity);

  UserDietaryRestrictionEntity domainToEntity(UserDietaryRestriction domain);

  @Mapping(target = "dietaryRestriction.id", source = "dietaryRestrictionId")
  UserDietaryRestriction requestToDomain(UserDietaryRestrictionRequestJson request);

  @Mapping(target = "userId", source = "user.id")
  UserDietaryRestrictionResponseJson domainToResponse(UserDietaryRestriction domain);

  List<UserDietaryRestrictionResponseJson> domainsToResponsesList(
      List<UserDietaryRestriction> domains);
}
