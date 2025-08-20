package com.fema.tcc.gateways;

import com.fema.tcc.domains.dietaryRestriction.DietaryRestriction;

public interface DietaryRestrictionGateway {

  DietaryRestriction findById(Integer id);

  boolean existsById(Integer id);
}
