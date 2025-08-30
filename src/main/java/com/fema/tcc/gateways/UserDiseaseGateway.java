package com.fema.tcc.gateways;

import com.fema.tcc.domains.disease.UserDisease;

import java.util.List;

public interface UserDiseaseGateway {

    UserDisease findById(Integer id);

    void save(UserDisease disease);

    List<UserDisease> findAllByUserId(Integer userId);
}
