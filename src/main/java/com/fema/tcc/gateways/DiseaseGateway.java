package com.fema.tcc.gateways;

import com.fema.tcc.domains.disease.Disease;

public interface DiseaseGateway {

    Disease findById(Integer id);
}
