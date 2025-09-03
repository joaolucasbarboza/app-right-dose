package com.fema.tcc.gateways;

import com.fema.tcc.domains.dietaryRestriction.DietaryRestriction;
import java.util.List;

public interface DietaryRestrictionGateway {

  DietaryRestriction findById(Integer id);

  boolean existsById(Integer id);

  List<DietaryRestriction> findAll();
}
