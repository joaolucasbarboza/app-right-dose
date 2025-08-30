package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.DiseaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiseaseRepository extends JpaRepository<DiseaseEntity, Integer> {
    List<DiseaseEntity> findByDescriptionContainingIgnoreCase(String description);
}
