package com.fema.tcc.gateways.impl;

import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.gateways.UserDietaryRestrictionGateway;
import com.fema.tcc.gateways.http.mappers.UserDietaryRestrictionJsonMapper;
import com.fema.tcc.gateways.postgresql.entity.UserDietaryRestrictionEntity;
import com.fema.tcc.gateways.postgresql.repository.UserDietaryRestrictionRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDietaryRestrictionImpl implements UserDietaryRestrictionGateway {

  private final UserDietaryRestrictionRepository repository;
  private final UserDietaryRestrictionJsonMapper mapper;

  @Override
  public List<UserDietaryRestriction> findAllByUserId(Integer userId) {
    List<UserDietaryRestrictionEntity> entities = repository.findAllByUser_Id(userId);
    return entities.stream().map(mapper::entityToDomain).toList();
  }

  @Override
  public void save(UserDietaryRestriction userDietaryRestriction) {
    UserDietaryRestrictionEntity entity = mapper.domainToEntity(userDietaryRestriction);
    repository.save(entity);
  }
}
