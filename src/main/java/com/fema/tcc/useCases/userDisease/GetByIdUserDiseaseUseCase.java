package com.fema.tcc.useCases.userDisease;

import com.fema.tcc.domains.disease.UserDisease;
import com.fema.tcc.gateways.UserDiseaseGateway;
import com.fema.tcc.useCases.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetByIdUserDiseaseUseCase {

  private final UserDiseaseGateway userDiseaseGateway;
  private final UserUseCase userUseCase;

  public UserDisease execute(Integer id) {
    UserDisease userDisease = userDiseaseGateway.findById(id);

    Integer userId = userUseCase.getCurrentUser();

    if (!userDisease.getUser().getId().equals(userId)) {
      throw new IllegalArgumentException(
          "User does not have permission to access this disease record.");
    }

    return userDisease;
  }
}
