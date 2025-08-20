package com.fema.tcc.gateways.impl;

import com.fema.tcc.domains.dietaryRestriction.DietaryRestriction;
import com.fema.tcc.gateways.DietaryRestrictionGateway;
import com.fema.tcc.gateways.http.mappers.DietaryRestrictionJsonMapper;
import com.fema.tcc.gateways.postgresql.entity.DietaryRestrictionEntity;
import com.fema.tcc.gateways.postgresql.repository.DietaryRestrictionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DietaryRestrictionGatewayImpl implements DietaryRestrictionGateway {

  private final DietaryRestrictionRepository repository;
  private final DietaryRestrictionJsonMapper mapper;

  @Override
  public DietaryRestriction findById(Integer id) {
    DietaryRestrictionEntity entity =
        repository
            .findById(id)
            .orElseThrow(
                () -> new RuntimeException("Dietary restriction not found with id: " + id));

    return mapper.entityToDomain(entity);
  }

  @Override
  public boolean existsById(Integer id) {
    return repository.existsById(id);
  }
}
