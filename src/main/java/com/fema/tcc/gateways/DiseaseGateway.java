package com.fema.tcc.gateways;

import com.fema.tcc.domains.disease.Disease;

import java.util.List;

public interface DiseaseGateway {

    Disease findById(Integer id);
    List<Disease> findAll();
}
