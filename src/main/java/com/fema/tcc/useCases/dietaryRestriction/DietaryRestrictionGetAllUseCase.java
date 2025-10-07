package com.fema.tcc.useCases.dietaryRestriction;

import com.fema.tcc.domains.dietaryRestriction.DietaryRestriction;
import com.fema.tcc.gateways.DietaryRestrictionGateway;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DietaryRestrictionGetAllUseCase {

  private final DietaryRestrictionGateway dietaryRestrictionGateway;

  public List<DietaryRestriction> execute() {
    return dietaryRestrictionGateway.findAll();
  }
}
