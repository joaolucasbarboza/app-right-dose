package com.fema.tcc.gateways;

import com.fema.tcc.domains.dietaryRestriction.UserDietaryRestriction;
import java.util.List;

public interface UserDietaryRestrictionGateway {

  List<UserDietaryRestriction> findAllByUserId(Integer userId);

  void save(UserDietaryRestriction userDietaryRestriction);
}
