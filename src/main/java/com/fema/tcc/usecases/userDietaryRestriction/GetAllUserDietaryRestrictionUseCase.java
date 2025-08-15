package com.fema.tcc.usecases.userDietaryRestriction;

import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import com.fema.tcc.gateways.UserDietaryRestrictionGateway;
import com.fema.tcc.usecases.user.UserUseCase;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetAllUserDietaryRestrictionUseCase {

  private final UserDietaryRestrictionGateway gateway;
  private final UserUseCase userUseCase;

  public List<UserDietaryRestriction> execute() {
    Integer userId = userUseCase.getCurrentUser();
    return gateway.findAllByUserId(userId);
  }
}
