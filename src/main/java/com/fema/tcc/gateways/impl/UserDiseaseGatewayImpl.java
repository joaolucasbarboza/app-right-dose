package com.fema.tcc.gateways.impl;

import com.fema.tcc.domains.disease.UserDisease;
import com.fema.tcc.gateways.UserDiseaseGateway;
import com.fema.tcc.gateways.http.mappers.UserDiseaseJsonMapper;
import com.fema.tcc.gateways.postgresql.entity.UserDiseaseEntity;
import com.fema.tcc.gateways.postgresql.repository.UserDiseaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDiseaseGatewayImpl implements UserDiseaseGateway {

    private final UserDiseaseRepository repository;
    private final UserDiseaseJsonMapper mapper;


    @Override
    public UserDisease findById(Integer id) {
        UserDiseaseEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserDisease not found"));

        return mapper.entityToDomain(entity);
    }

    @Override
    public void save(UserDisease userDisease) {
        UserDiseaseEntity entity = mapper.domainToEntity(userDisease);
        repository.save(entity);
    }
}
