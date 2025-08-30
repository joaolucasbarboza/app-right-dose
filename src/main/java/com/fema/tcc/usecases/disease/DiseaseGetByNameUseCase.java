package com.fema.tcc.usecases.disease;

import com.fema.tcc.domains.disease.Disease;
import com.fema.tcc.gateways.DiseaseGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiseaseGetByNameUseCase {

    private final DiseaseGateway diseaseGateway;

    public List<Disease> execute() {
        return diseaseGateway.findAll();
    }
}
