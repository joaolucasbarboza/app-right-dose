package com.fema.tcc.gateways;

import com.fema.tcc.domains.disease.UserDisease;

public interface UserDiseaseGateway {

    UserDisease findById(Integer id);

    void save(UserDisease disease);
}
