package com.fema.tcc.usecases.userDisease;

import com.fema.tcc.domains.disease.UserDisease;
import com.fema.tcc.gateways.UserDiseaseGateway;
import com.fema.tcc.usecases.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
