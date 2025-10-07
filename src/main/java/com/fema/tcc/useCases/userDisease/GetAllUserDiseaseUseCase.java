package com.fema.tcc.useCases.userDisease;

import com.fema.tcc.domains.disease.UserDisease;
import com.fema.tcc.gateways.UserDiseaseGateway;
import com.fema.tcc.useCases.user.UserUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllUserDiseaseUseCase {

  private final UserDiseaseGateway userDiseaseGateway;
  private final UserUseCase userUseCase;

  public List<UserDisease> execute() {
    Integer userId = userUseCase.getCurrentUser();
    return userDiseaseGateway.findAllByUserId(userId);
  }
}
