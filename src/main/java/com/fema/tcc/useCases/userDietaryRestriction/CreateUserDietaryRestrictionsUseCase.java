package com.fema.tcc.useCases.userDietaryRestriction;

import com.fema.tcc.domains.dietaryRestriction.DietaryRestriction;
import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.DietaryRestrictionGateway;
import com.fema.tcc.gateways.UserDietaryRestrictionGateway;
import com.fema.tcc.useCases.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserDietaryRestrictionsUseCase {

  private final UserDietaryRestrictionGateway userDietaryRestrictionGateway;
  private final DietaryRestrictionGateway dietaryRestrictionGateway;
  private final UserUseCase userUseCase;

  public void execute(UserDietaryRestriction userDietaryRestriction) {

    try {
      Integer dietaryRestrictionId = userDietaryRestriction.getDietaryRestriction().getId();

      DietaryRestriction dietaryRestriction =
          dietaryRestrictionGateway.findById(dietaryRestrictionId);
      userDietaryRestriction.setDietaryRestriction(dietaryRestriction);

      User user = userUseCase.getUser();
      userDietaryRestriction.setUser(user);

      userDietaryRestrictionGateway.save(userDietaryRestriction);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
