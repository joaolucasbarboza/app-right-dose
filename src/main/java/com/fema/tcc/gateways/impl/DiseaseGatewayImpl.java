package com.fema.tcc.gateways.impl;

import com.fema.tcc.domains.disease.Disease;
import com.fema.tcc.gateways.DiseaseGateway;
import com.fema.tcc.gateways.http.mappers.DiseaseJsonMapper;
import com.fema.tcc.gateways.postgresql.entity.DiseaseEntity;
import com.fema.tcc.gateways.postgresql.repository.DiseaseRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DiseaseGatewayImpl implements DiseaseGateway {

  private final DiseaseRepository repository;
  private final DiseaseJsonMapper mapper;

  @Override
  public Disease findById(Integer id) {
    DiseaseEntity entity =
        repository.findById(id).orElseThrow(() -> new RuntimeException("Disease not found"));

    return mapper.entityToDomain(entity);
  }

  @Override
  public List<Disease> findAll() {
    List<DiseaseEntity> entities = repository.findAll();

    return entities.stream().map(mapper::entityToDomain).toList();
  }
}
