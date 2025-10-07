package com.fema.tcc.usecases.userDisease;

import com.fema.tcc.domains.disease.Disease;
import com.fema.tcc.domains.disease.UserDisease;
import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.DiseaseGateway;
import com.fema.tcc.gateways.UserDiseaseGateway;
import com.fema.tcc.usecases.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserDiseaseUserCase {

  private final UserDiseaseGateway userDiseaseGateway;
  private final DiseaseGateway diseaseGateway;
  private final UserUseCase userUseCase;

  public void execute(UserDisease userDisease) {

    try {
      Integer diseaseId = userDisease.getDisease().getId();

      Disease disease = diseaseGateway.findById(diseaseId);
      userDisease.setDisease(disease);

      User user = userUseCase.getUser();
      userDisease.setUser(user);

      userDiseaseGateway.save(userDisease);
    } catch (RuntimeException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
