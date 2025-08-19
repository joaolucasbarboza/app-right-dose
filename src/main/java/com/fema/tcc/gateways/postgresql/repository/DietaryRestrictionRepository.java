package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.DietaryRestrictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietaryRestrictionRepository
    extends JpaRepository<DietaryRestrictionEntity, Integer> {}
