package com.fema.tcc.usecases.disease;

import com.fema.tcc.domains.disease.Disease;
import com.fema.tcc.gateways.DiseaseGateway;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiseaseGetByNameUseCase {

  private final DiseaseGateway diseaseGateway;

  public List<Disease> execute() {
    return diseaseGateway.findAll();
  }
}
