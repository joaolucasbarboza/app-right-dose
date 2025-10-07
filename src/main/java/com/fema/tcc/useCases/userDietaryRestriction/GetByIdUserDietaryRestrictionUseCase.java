package com.fema.tcc.useCases.userDietaryRestriction;

import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.gateways.UserDietaryRestrictionGateway;
import com.fema.tcc.useCases.user.UserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetByIdUserDietaryRestrictionUseCase {

  private final UserDietaryRestrictionGateway gateway;
  private final UserUseCase userUseCase;

  public UserDietaryRestriction execute(Integer id) {
    Integer userId = userUseCase.getCurrentUser();
    UserDietaryRestriction userDietaryRestriction = gateway.findById(id);

    if (!userDietaryRestriction.getUser().getId().equals(userId)) {
      throw new IllegalArgumentException(
          "User does not have permission to access this dietary restriction");
    }

    return gateway.findById(id);
  }
}
