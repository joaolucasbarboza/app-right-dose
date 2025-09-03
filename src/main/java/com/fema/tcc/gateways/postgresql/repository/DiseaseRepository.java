package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.DiseaseEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseRepository extends JpaRepository<DiseaseEntity, Integer> {
  List<DiseaseEntity> findByDescriptionContainingIgnoreCase(String description);
}
