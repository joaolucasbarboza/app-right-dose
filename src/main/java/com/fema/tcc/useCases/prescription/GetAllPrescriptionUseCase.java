package com.fema.tcc.useCases.prescription;

import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.gateways.PrescriptionGateway;
import com.fema.tcc.useCases.user.UserUseCase;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetAllPrescriptionUseCase {

  private final PrescriptionGateway gateway;
  private final UserUseCase userUseCase;

  public List<Prescription> execute() {
    Integer userId = userUseCase.getCurrentUser();
    return gateway.findAllByUserId(userId);
  }
}
